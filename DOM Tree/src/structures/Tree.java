package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		if(sc == null){
			return;
		}
		
		Stack<TagNode> tagStack = new Stack<TagNode>();
		String currentLine = sc.nextLine();
		root = new TagNode("html", null, null);
		tagStack.push(root);
		
		while(sc.hasNextLine()){
			currentLine = sc.nextLine();
			if(currentLine.contains("<") && currentLine.contains(">") && currentLine.contains("/")){
				tagStack.pop();
			}else if(currentLine.contains("<") && currentLine.contains(">") && !currentLine.contains("/")){
				if(tagStack.peek().firstChild == null){
					TagNode builder = new TagNode(currentLine.replace("<", "").replace(">", ""), null, null);
					tagStack.peek().firstChild = builder;
					tagStack.push(builder);
				}else{
					TagNode currentTag = tagStack.peek().firstChild;
					while(currentTag.sibling != null)	//get the sibling most to the right
						currentTag = currentTag.sibling;
						TagNode builder = new TagNode(currentLine.replace("<", "").replace(">", ""), null, null);
						currentTag.sibling = builder;
						tagStack.push(builder);
				}
			}else{
				if(tagStack.peek().firstChild == null){
					tagStack.peek().firstChild = new TagNode(currentLine, null, null);
				}else{
					TagNode currentTag = tagStack.peek().firstChild;
					while(currentTag.sibling != null)
						currentTag = currentTag.sibling;
					
				currentTag.sibling = new TagNode(currentLine, null, null);
				}
			}
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag
	 *            Old tag
	 * @param newTag
	 *            Replacement tag
	 */

	public void replaceTag(String oldTag, String newTag){	
		if(root == null || oldTag == null || newTag == null){
			return;
		}else{
			replaceRecurse(oldTag, newTag, root.firstChild);
		}
	}
	private void replaceRecurse(String oldTag, String newTag, TagNode tempNode){
		if(tempNode == null){
			return;
		}
		else if(tempNode.tag.compareTo(oldTag) == 0){
			tempNode.tag = newTag;
		}
		this.replaceRecurse(oldTag, newTag, tempNode.firstChild);
		this.replaceRecurse(oldTag, newTag, tempNode.sibling);
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The
	 * boldface (b) tag appears directly under the td tag of every column of
	 * this row.
	 * 
	 * @param row
	 *            Row to bold, first row is numbered 1 (not 0).
	 */
	
	public void boldRow(int row){
		if(row <= 0){
			return;
		}else{
			this.boldRecurse(row, 0, root, root.firstChild);
		}
	}
	private void boldRecurse(int row, int count, TagNode prevNode, TagNode tempNode){
		if(tempNode == null){
			return;
		}else if(tempNode.tag.equals("tr")){
			count++;
		}
		if(count == row && tempNode.firstChild == null){
			prevNode.firstChild = new TagNode("b", tempNode, null);
		}
		boldRecurse(row, count, tempNode, tempNode.firstChild);
		boldRecurse(row, count, tempNode, tempNode.sibling);
	}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em,
	 * or b, all occurrences of the tag are removed. If the tag is ol or ul,
	 * then All occurrences of such a tag are removed from the tree, and, in
	 * addition, all the li tags immediately under the removed tag are converted
	 * to p tags.
	 * 
	 * @param tag
	 *            Tag to be removed, can be p, em, b, ol, or ul
	 */

	public void removeTag(String tag){
		if(root == null){
			return;
		}else{
			while(this.hasTag(tag, root)){
				removeRecurse(tag, root, root.firstChild);
			}
		}
	}
	private void removeRecurse(String tag, TagNode prevNode, TagNode tempNode){
		if(tempNode == null || prevNode == null){
			return;
		}else if(tempNode.tag.equals(tag)){
			if(tag.equals("ul") || tag.equals("ol")){
				this.removeListTag(tempNode.firstChild);
			}
			if(prevNode.firstChild == tempNode){
				prevNode.firstChild = tempNode.firstChild; 
				this.addLastSibling(tempNode.firstChild, tempNode.sibling);
			}else if(prevNode.sibling == tempNode){
				this.addLastSibling(tempNode.firstChild, tempNode.sibling);
				prevNode.sibling = tempNode.firstChild;
			}
			return;
		}
		prevNode = tempNode;
		removeRecurse(tag, prevNode, tempNode.firstChild);
		removeRecurse(tag, prevNode, tempNode.sibling);
	}
	private void removeListTag(TagNode tempNode){
		if(tempNode == null){
			return;
		}else if(tempNode.tag.compareTo("li") == 0){
			tempNode.tag = "p";
		}
		this.removeListTag(tempNode.sibling);
	}
	private void addLastSibling(TagNode tempNode, TagNode newSibling){
		while(tempNode.sibling != null){	//gets last sibling
			tempNode = tempNode.sibling;
		}
		tempNode.sibling = newSibling;
	}
	private boolean hasTag(String tag, TagNode tempNode){
		if(tempNode == null){
			return false;
		}else if(tempNode.tag.compareTo(tag) == 0){
			return true;
		}
		return this.hasTag(tag, tempNode.firstChild) || this.hasTag(tag, tempNode.sibling);
	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word
	 *            Word around which tag is to be added
	 * @param tag
	 *            Tag to be added
	 */
	public void addTag(String word, String tag) {
		if (root == null || word == null || tag == null) {
			return;
		} else {
			this.addTagRecurse(word, tag, root.firstChild);
		}
	}

	private void addTagRecurse(String word, String tag, TagNode tempNode){
		if(tempNode == null){
			return;
		}else if(tempNode.tag.toLowerCase().contains(word.toLowerCase())){
			if(tempNode.tag.equalsIgnoreCase(word)){
				tempNode.tag = tag;
				tempNode.firstChild = new TagNode(word, tempNode.firstChild, null);
			}else if(tempNode.tag.toLowerCase().contains(word.toLowerCase())){
				TagNode sibling = tempNode.sibling;
				String preSub = tempNode.tag.substring(0, tempNode.tag.toLowerCase().indexOf(word.toLowerCase()));
				String postSub = tempNode.tag.substring(tempNode.tag.toLowerCase().indexOf(word.toLowerCase()) + word.length());
				String punctuation = "";
				String original = tempNode.tag.substring(tempNode.tag.toLowerCase().indexOf(word.toLowerCase()), tempNode.tag.toLowerCase().indexOf(word.toLowerCase()) + word.length());
				if(postSub.length() > 0){
					if(postSub.length() > 1 && (this.checkPunctuation(postSub.charAt(0)) && !this.checkPunctuation(postSub.charAt(1)))){
						punctuation = "" + postSub.charAt(0);
						postSub = postSub.substring(1);
					}
				}
				if(postSub.length() == 0 || (postSub.length() >= 1 && (postSub.charAt(0) == ' ' || this.checkPunctuation(postSub.charAt(0))))){
					if(postSub.equals("!") || postSub.equals(",") || postSub.equals(".") || postSub.equals("?") || postSub.equals(":") || postSub.equals(";")){
						original = original + postSub;
						postSub = "";
					}
					tempNode.tag = preSub;
					tempNode.sibling = new TagNode(tag, new TagNode(original + punctuation, null, null), null);
					if(postSub.length() > 0){
						if(sibling != null){
							tempNode.sibling.sibling = new TagNode(postSub, null, sibling);	
						}else{
							tempNode.sibling.sibling = new TagNode(postSub, null, null);
						}
					}else if(sibling != null){
						tempNode.sibling.sibling = sibling;
					}
				}
			}
			this.addTagRecurse(word, tag, tempNode.sibling.sibling);
		}else{
			this.addTagRecurse(word, tag, tempNode.firstChild);
			this.addTagRecurse(word, tag, tempNode.sibling);
		}
	}

	private boolean checkPunctuation(char x){
		return(x == '.' || x == '!' || x == '?' || x == ',' || x == ':' || x == ';');
	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the input
	 * file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}

