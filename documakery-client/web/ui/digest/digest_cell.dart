library digest_cell;

import 'dart:html';
import 'package:web_ui/web_ui.dart';

import 'package:documakery/string_utils.dart';

import '../../model/model.dart';

class DigestCell extends WebComponent {
  QuestionBlock questionBlock;
  
  @observable
  String title;
  
  @observable
  String snippet;
  
  DigestCell(this.questionBlock) {
    title = trimToSize(questionBlock.title, 50);
    snippet = trimToSize(questionBlock.introduction, 100);
  }
}

