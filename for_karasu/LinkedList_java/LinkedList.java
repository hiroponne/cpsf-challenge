package jp.ac.keio.sfc.ht.cpsf.hiropon.challenge.linkedlist;

public class LinkedList<E> {

	private DoublyLinkedList head;
	private int length;

	public LinkedList() {
		head = null;
		length = 0;
	}

	public boolean add(E element) {
		DoublyLinkedList doublyLinkedList = new DoublyLinkedList();
		/** 1つ目の要素の場合はheadに設定してnextとpreviousを自身に設定 */
		if (this.head == null) {
			this.head = doublyLinkedList;
			doublyLinkedList.next = doublyLinkedList;
			doublyLinkedList.previous = doublyLinkedList;
		} else {
			/** headの1つ前, つまり最後の要素に要素を追加して繋がるlistの要素をそれぞれ変更 */
			head.previous.next = doublyLinkedList;
			doublyLinkedList.previous = head.previous;
			doublyLinkedList.next = head;
			head.previous = doublyLinkedList;
		}
		doublyLinkedList.element = element;
		this.length++;
		return true;
	}

	public void add(int index, E element) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			DoublyLinkedList doublyLinkedList = new DoublyLinkedList();
			/** headがnullの時の処理 */
			if (this.head == null) {
				this.head = doublyLinkedList;
				doublyLinkedList.next = doublyLinkedList;
				doublyLinkedList.previous = doublyLinkedList;
			} else {
				/** listをheadに指定 */
				DoublyLinkedList list = head;
				/** index番の要素まで移動 */
				for (int i = 0; i < index; i++)
					list = list.next;
				if (list.equals(head))
					head = doublyLinkedList;
				list.previous.next = doublyLinkedList;
				doublyLinkedList.previous = list.previous;
				doublyLinkedList.next = list;
				list.previous = doublyLinkedList;
			}
			doublyLinkedList.element = element;
			this.length++;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/** 要素を先頭に追加 */
	public void addFirst(E element) {
		add(0, element);
	}

	/** 一番後に要素を追加 */
	public void addLast(E element) {
		add(element);
	}

	/** headを初期化する事で全部消せる */
	public void clear() {
		head = null;
	}

	/** index番のよそを取得 */
	public E get(int index) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			DoublyLinkedList list = head;
			for (int i = 0; i < index; i++)
				list = list.next;
			return (E) list.element;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** elementと同じ要素の番号を先頭から探す */
	public int indexOf(E element) {
		DoublyLinkedList list = head;
		if (element == null) {
			for (int i = 0; i < this.length; i++) {
				if (list.element == null)
					return i;
				list = list.next;
			}
		} else {
			for (int i = 0; i < this.length; i++) {
				if (element.equals(list.element))
					return i;
				list = list.next;
			}
		}
		return -1;
	}

	/** elementと同じ要素の番号を後から探す */
	public int lastIndexOf(E element) {
		DoublyLinkedList list = head.previous;
		if (element == null) {
			for (int i = this.length - 1; i >= 0; i--) {
				if (list.element == null)
					return i;
				list = list.previous;
			}
		} else {
			for (int i = this.length - 1; i >= 0; i--) {
				if (element.equals(list.element))
					return i;
				list = list.previous;
			}
		}
		return -1;
	}

	/** index番の要素を消去 */
	public E remove(int index) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			DoublyLinkedList list = head;
			for (int i = 0; i < index; i++, list = list.next) {
			}
			if (list.equals(head))
				head = head.next;
			E tmp = list.element;
			list.previous.next = list.next;
			list.next.previous = list.previous;
			this.length--;
			return tmp;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	public E set(int index, E element) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			DoublyLinkedList list = head;
			for (int i = 0; i < index; i++, list = list.next) {
			}
			E tmp = list.element;
			list.element = element;
			return tmp;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int size() {
		return this.length;
	}

	/** 双方向リスト, 前と次の情報と要素を格納している */
	private class DoublyLinkedList {
		DoublyLinkedList next, previous;
		E element;

		private DoublyLinkedList() {
		}
	}
}
