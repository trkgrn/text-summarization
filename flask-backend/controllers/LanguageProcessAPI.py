from flask import jsonify, Blueprint, request
from utils import GloveUtil
from services import LanguageProcessService

language_process_bp = Blueprint('language_process_bp', __name__)


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
    return jsonify(processed_document)


@language_process_bp.route('/api/v1/language-process/document/sentence-scores', methods=['POST'])
def sentences_scores():
    document = request.json
    print(document)
    processed_document = LanguageProcessService.calculate_sentence_score_by_document(document)
    return jsonify(processed_document)


@language_process_bp.route('/api/v1/language-process/document/rouge-score', methods=['POST'])
def calculate_rouge_score():
    document = request.json
    summary = document['summary']
    reference = document['reference']
    document['rougeScore'] = LanguageProcessService.calculate_rouge_score(summary, reference)
    return jsonify(document)
