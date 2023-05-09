import numpy as np
import re
import scipy
from nltk.corpus import stopwords
from utils import StringUtil


def preprocess(raw_text):

    # keep only words
    letters_only_text = re.sub("[^a-zA-Z]", " ", raw_text)

    # convert to lower case and split
    words = letters_only_text.lower().split()

    # remove stopwords
    stopword_set = set(stopwords.words("english"))
    cleaned_words = list([w for w in words if w not in stopword_set])
    # print("cleaned_words =" + cleaned_words.__str__())

    return cleaned_words


def cosine_distance_wordembedding_method(s1, s2):
    vector_1 = np.mean([model[word] for word in preprocess(s1)], axis=0)
    vector_2 = np.mean([model[word] for word in preprocess(s2)], axis=0)
    cosine = scipy.spatial.distance.cosine(vector_1, vector_2)
    return round((1 - cosine) * 100, 2)


def loadGloveModel(gloveFile):
    print("Loading Glove Model")
    with open(gloveFile, encoding="utf8") as f:
        content = f.readlines()
    model = {}
    for line in content:
        splitLine = line.split()
        word = splitLine[0]
        embedding = np.array([float(val) for val in splitLine[1:]])
        model[word] = embedding
    print("Done.", len(model), " words loaded!")
    return model


model = loadGloveModel('glove.6B.50d.txt')

