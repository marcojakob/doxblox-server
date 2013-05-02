library tree_node_tests;

import 'package:unittest/unittest.dart';
import 'package:unittest/html_config.dart';

import '../../../web/ui/navigation/tree_view.dart';

main() {
useHtmlConfiguration();
  
group('TreeNode Tests:', () {

test('equals_SameNode_ReturnsTrue', () {
  // given
  TreeNode node1 = new TreeNode('1', 'aaa', 'document');
  TreeNode node2 = new TreeNode('1', 'aaa', 'document');
  
  // when
  bool same = node1 == node2;
  
  // then
  expect(same, isTrue);
});

test('equals_NotSameNode_ReturnsFalse', () {
  // given
  TreeNode node1 = new TreeNode('1', 'aaa', 'document');
  TreeNode node2 = new TreeNode('1', 'aba', 'document');
  
  // when
  bool same = node1 == node2;
  
  // then
  expect(same, isFalse);
});

test('equals_CompareToNull_ReturnsFalse', () {
  // given
  TreeNode node1 = new TreeNode('1', 'aaa', 'document');
  TreeNode node2 = null;
  
  // when
  bool same = node1 == node2;
  
  // then
  expect(same, isFalse);
});

test('hashCode_SameNode_ReturnsSameHashCode', () {
  // given
  TreeNode node1 = new TreeNode('1', 'aaa', 'document');
  TreeNode node2 = new TreeNode('1', 'aaa', 'document');
  
  // when
  int hash1 = node1.hashCode;
  int hash2 = node2.hashCode;
  
  // then
  expect(hash1, equals(hash2));
});

test('hashCode_NotSameNode_ReturnsNotSameHashCode', () {
  // given
  TreeNode node1 = new TreeNode('1', 'aaa', 'document');
  TreeNode node2 = new TreeNode('1', 'aba', 'document');
  
  // when
  int hash1 = node1.hashCode;
  int hash2 = node2.hashCode;
  
  // then
  expect(hash1, isNot(equals(hash2)));
});

});
}

