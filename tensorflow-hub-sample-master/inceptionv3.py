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

vailed_ext = [".jpg",".png"]
image_dict = {}

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

def adjust_npimage(data_path):
    images = []
    for path in data_path:
        value = cv2.imread(path)
        resized_img = np.ravel(value)
        resized_img = resized_img.astype(np.float32)/255
        images.append(resized_img)

    return np.array(images)

def adjust_image(data):
    imgs = tf.reshape(data, [-1, 960, 720, 3])
    # Adjust image size to that in Inception-v3 input.
    imgs = tf.image.resize_images(imgs, (299, 299))

    return imgs

def image_lists(rootDir,filenames):
    for lists in os.listdir(rootDir):
        path = os.path.join(rootDir, lists)
        filename, file_extension = os.path.splitext(path)
        if file_extension in vailed_ext:
            image_dict[path]=filenames.index(os.path.basename(os.path.split(path)[0]))
        if os.path.isdir(path):
            image_lists(path,filenames)

def main(unused_argv):
    # if not FLAGS.image_dir:
    #     tf.logging.error('Must set flag --image_dir.')
    #     return -1

    with tf.Graph().as_default() as g:
        # image_lists(FLAGS.image_dir)
        class_names = os.listdir("C:\\Users\\lamie\\PycharmProjects\\retrain\\data")
        image_lists("C:\\Users\\lamie\\PycharmProjects\\retrain\\data", class_names)

        image_path = list(image_dict.keys())
        image_labels = list(image_dict.values())


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