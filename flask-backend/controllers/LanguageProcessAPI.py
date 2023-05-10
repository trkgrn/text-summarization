from flask import jsonify, Blueprint, request
from utils import GloveUtil
from services import LanguageProcessService
from kafka_producers import MessageProducer

language_process_bp = Blueprint('language_process_bp', __name__)


producer = MessageProducer.MessageProducer()

@language_process_bp.route('/api/v1/language-process', methods=['POST'])
def similarity():
    ss1 = request.json.get('s1')
    ss2 = request.json.get('s2')
    s = GloveUtil.cosine_distance_wordembedding_method(ss1, ss2)
    return jsonify(similarity=s)


@language_process_bp.route('/api/v1/language-process/document/similarities', methods=['POST'])
def similarities():
    document = request.json
    print(document)
    processed_document = LanguageProcessService.get_similarities_by_document(document)
    name = document['name']
    producer.send_msg("name="+name+",stage=STEP_2")
    return jsonify(processed_document)


@language_process_bp.route('/api/v1/language-process/document/sentence-scores', methods=['POST'])
def sentences_scores():
    document = request.json
    print(document)
    processed_document = LanguageProcessService.calculate_sentence_score_by_document(document)
    name = document['name']
    producer.send_msg("name="+name+",stage=STEP_3")
    return jsonify(processed_document)


@language_process_bp.route('/api/v1/language-process/document/rouge-score', methods=['POST'])
def calculate_rouge_score():
    document = request.json
    summary = document['summary']
    reference = document['reference']
    document['rougeScore'] = LanguageProcessService.calculate_rouge_score(summary, reference)
    name = document['document']['name']
    producer.send_msg("name="+name+",stage=STEP_5")
    return jsonify(document)
