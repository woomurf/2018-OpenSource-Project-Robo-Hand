import argparse
import collections
import hashlib
import os.path
import re
import random
import numpy as np
import cv2

import tensorflow as tf
import tensorflow_hub as hub

FLAGS = None

tf.logging.set_verbosity(tf.logging.INFO)   #logging을 보기 위해

def inceptionv3_model_fn(features, labels, mode):

    # hub를 통해 Inception v3 모델을 불러옴
    module = hub.Module("https://tfhub.dev/google/imagenet/inception_v3/feature_vector/1")

    # Inception V3의 경우 입력은 299x299x3, 출력은 2048차원 벡터
    input_layer = adjust_image(features["x"])
    outputs = module(input_layer)

    """
    Estimator API
    tf.layers.dense(inputs, units, activation)
    inputs: 앞의 레이어, units: 레이어의 크기 정의, activation: sigmoid, ReLu 함수 정의
    """
    logits = tf.layers.dense(inputs=outputs, units=10)

    predictions = {
        # Generate predictions (for PREDICT and EVAL mode)
        "classes": tf.argmax(input=logits, axis=1),
        # Add `softmax_tensor` to the graph. It is used for PREDICT and by the
        # `logging_hook`.
        "probabilities": tf.nn.softmax(logits, name="softmax_tensor")
    }

    if mode == tf.estimator.ModeKeys.PREDICT:   # mode가 PREDICT 일때
        return tf.estimator.EstimatorSpec(mode=mode, predictions=predictions)   # 예측된 값을 dict 형태로 리턴한다.


    # Calculate Loss (for both TRAIN and EVAL modes)    # mode가 train인 경우
    loss = tf.losses.sparse_softmax_cross_entropy(labels=labels, logits=logits) # 경사하강법 훈련

    # Configure the Training Op (for TRAIN mode)
    if mode == tf.estimator.ModeKeys.TRAIN:
        optimizer = tf.train.GradientDescentOptimizer(learning_rate=0.001)
        train_op = optimizer.minimize(
            loss=loss,
            global_step=tf.train.get_global_step())
        return tf.estimator.EstimatorSpec(mode=mode, loss=loss, train_op=train_op)

    # Add evaluation metrics (for EVAL mode)
    eval_metric_ops = {
        "accuracy": tf.metrics.accuracy(
            labels=labels, predictions=predictions["classes"])}
    return tf.estimator.EstimatorSpec(
        mode=mode, loss=loss, eval_metric_ops=eval_metric_ops)


def adjust_image(data):

    imgs = tf.reshape(data, [-1, 960, 720, 3])
    # Adjust image size to that in Inception-v3 input.
    imgs = tf.image.resize_images(imgs, (299, 299))

    return imgs

def main(unused_argv):
    # if not FLAGS.image_dir:
    #     tf.logging.error('Must set flag --image_dir.')
    #     return -1

    with tf.Graph().as_default() as g:
        # Load MNIST data.
        mnist = tf.contrib.learn.datasets.load_dataset("mnist")
        train_data = mnist.train.images  # Returns np.array # Returns np.array # shape (55000,784)
        train_labels = np.asarray(mnist.train.labels, dtype=np.int32)   # train_labels = {ndarray} [7 3 4 ... 5 6 8], shape (55000,)
        eval_data = mnist.test.images  # Returns np.array # shape (10000,784)
        eval_labels = np.asarray(mnist.test.labels, dtype=np.int32)

        # create input functions for train and evaluate methods.
        train_input_fn = tf.estimator.inputs.numpy_input_fn(
            x={"x": train_data},
            y=train_labels,
            batch_size=10,
            num_epochs=None,
            shuffle=True)
        eval_input_fn = tf.estimator.inputs.numpy_input_fn(
            x={"x": eval_data},
            y=eval_labels,
            num_epochs=1,
            shuffle=False)
                              
        # Create an estimator
        classifier = tf.estimator.Estimator(
            model_fn=inceptionv3_model_fn, model_dir="/tmp/convnet_model")

        # Set up logging for predictions
        tensors_to_log = {"probabilities": "softmax_tensor"}
        logging_hook = tf.train.LoggingTensorHook(
            tensors=tensors_to_log, every_n_iter=10)

        # Train network.
        classifier.train(
            input_fn=train_input_fn,
            steps=500,
            hooks=[logging_hook])

        # Evaluate the model and print results.
        eval_results = classifier.evaluate(input_fn=eval_input_fn)
        print(eval_results)


if __name__ == "__main__":
    # parser = argparse.ArgumentParser()
    # parser.add_argument(
    #     '--image_dir',
    #     type=str,
    #     default='',
    #     help='Path to folders of labeled images.'
    # )
    tf.app.run()