import spacy
from nltk.corpus import stopwords
import re
from rouge import Rouge

def get_proper_names_by_sentence(sentence):
    nlp = spacy.load("en_core_web_sm")
    doc = nlp(sentence)
    proper_names = []
    for ent in doc.ents:
        words = ent.text.split(" ")
        if len(words) > 1:
            for word in words:
                proper_names.append(word)
        else:
            proper_names.append(ent.text)
    return proper_names


def get_numeric_values_by_sentence(sentence):
    nlp = spacy.load("en_core_web_sm")
    doc = nlp(sentence)
    numeric_values = []
    for token in doc:
        if token.like_num:
            numeric_values.append(token.text)
    return numeric_values


def get_theme_words_by_sentence(sentence, theme_words):
    sentence = preprocess(sentence)
    words = []
    for word in sentence:
        if word in theme_words:
            words.append(word)
    return words


def get_words_in_title_by_sentence(title, sentence):
    stopword_set = set(stopwords.words("english"))
    title = punctuation_removal(title).split(" ")
    sentence = punctuation_removal(sentence).split(" ")
    title = list(set([w.lower() for w in title if w.lower() not in stopword_set]))
    sentence = list(set([w.lower() for w in sentence if w.lower() not in stopword_set]))
    words = []
    for word in sentence:
        if word in title:
            words.append(word)
    return words


def punctuation_removal(text):
    return re.sub(r'[^\w\s]', '', text)


def get_theme_words_by_sentences(sentences):
    sentences = [listToString(preprocess(s)) for s in sentences]
    tf_idfs = calculate_tf_idfs(sentences)
    count = (tf_idfs.keys().__len__() / 10).__round__()
    keys = dict_to_list(tf_idfs)
    words = []
    for i in range(0, count):
        words.append(keys[i])
    return words


def calculate_tf_idfs(sentences):
    from sklearn.feature_extraction.text import TfidfVectorizer
    import pandas as pd
    vectorizer = TfidfVectorizer(use_idf=True)
    tf_idf_matrix = vectorizer.fit_transform(sentences)
    df = pd.DataFrame(tf_idf_matrix[0].T.todense(), index=vectorizer.get_feature_names_out(), columns=["TF-IDF"])
    df = df.sort_values('TF-IDF', ascending=False)
    tf_idfs = {df.index.tolist()[i]: df.values.tolist()[i][0] for i in range(len(df.index.tolist()))}
    return tf_idfs


def extraction_numeric_values_for_sentence(sentence):
    sentence = punctuation_removal(sentence)
    numeric_values = get_numeric_values_by_sentence(sentence)
    sentence = sentence.split(" ")
    for word in sentence:
        if word in numeric_values:
            sentence.remove(word)
    return sentence


def preprocess(raw_text):
    # remove numeric values
    raw_text = listToString(extraction_numeric_values_for_sentence(raw_text))

    # keep only words
    letters_only_text = re.sub("[^a-zA-Z]", " ", raw_text)

    # convert to lower case and split
    words = letters_only_text.lower().split()

    # remove stopwords
    stopword_set = set(stopwords.words("english"))
    cleaned_words = list([w for w in words if w not in stopword_set])

    return cleaned_words


def listToString(s):
    str = " "
    return str.join(s)


def dict_to_list(d):
    l = []
    for k, v in d.items():
        l.append(k)
    return l


def calculate_sentence_score(sentence, theme_words, title, total_node_count):
    s_text = sentence["text"]

    s_proper_names = get_proper_names_by_sentence(s_text).__len__()
    s_numeric_values = get_numeric_values_by_sentence(s_text).__len__()
    s_exceeded_edge_count = sentence["numberOfEdgeExceedingThreshold"]
    s_title_words = get_words_in_title_by_sentence(title, s_text).__len__()
    s_theme_words = get_theme_words_by_sentence(s_text, theme_words).__len__()
    s_len = preprocess(s_text).__len__()

    score = (0.15 * (s_proper_names / s_len)) + (0.05 * (s_numeric_values / s_len)) + (
                0.30 * (s_exceeded_edge_count / total_node_count)) + (0.25 * (s_title_words / s_len)) + (
                        0.25 * (s_theme_words / s_len))

    return round(score * 100, 2)


def calculate_rouge_score(summary, reference):
    rouge = Rouge()
    scores = rouge.get_scores(summary, reference)
    return round(scores[0]["rouge-1"]["f"] * 100, 2)
