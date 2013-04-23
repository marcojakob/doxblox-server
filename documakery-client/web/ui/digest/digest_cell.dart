library digest_cell;

import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';

class DigestCell extends WebComponent {
  DocumentBlock documentBlock;
  
  @observable
  String title;
  @observable
  String snippet;
  @observable
    
  DigestCell(this.documentBlock) {
    title = documentBlock.digestTitle;
    snippet = documentBlock.digestSnippet;
  }
}

