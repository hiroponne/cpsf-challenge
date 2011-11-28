package jp.ac.keio.sfc.ht.cpsf.hiropon.challenge.arraylist;

public class ArrayList<E> {

	/** 要素の入れ子 */
	private Object elementData[];
	/** 入れ子の大きさ */
	private int length;

	public ArrayList(int capacity) {
		elementData = new Object[capacity];
		length = 0;
	}

	public ArrayList() {
		this(10);
	}

	/** 要素を追加 */
	public boolean add(E element) {
		/** 容量を拡張 */
		ensureCapacity();
		/** elementを追加してlengthを+1 */
		elementData[this.length++] = element;
		return true;
	}

	/** index番に要素を追加, index番以降の要素は１つずつずれる */
	public void add(int index, E element) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			/** 容量を拡張 */
			ensureCapacity();
			/** index番以降の要素を1つずつ後にずらす */
			System.arraycopy(elementData, index, elementData, index + 1,
					this.length - index);
			/** index番に要素を格納 */
			elementData[index] = element;
			/** length+1 */
			this.length++;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/** 要素を全て消してlength=0 */
	public void clear() {
		for (int i = 0; i < this.length; i++)
			elementData[i] = null;
		this.length = 0;
	}

	/** index番の情報を取得 */
	public E get(int index) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			return (E) elementData[index];
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** elementと一致する番号を頭から探す */
	public int indexOf(E element) {
		/** elementがnullの時のやつ */ 
		if (element == null) {
			for (int i = 0; i < this.length; i++)
				if (elementData[i] == null)
					return i;
		} else {
			for (int i = 0; i < this.length; i++)
				if (element.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/** elementと一致する番号を後から探す */
	public int lastIndexOf(E element) {
		/** elementがnullの時のやつ */ 
		if (element == null) {
			for (int i = this.length - 1; i >= 0; i--)
				if (elementData[i] == null)
					return i;
		} else {
			for (int i = this.length - 1; i >= 0; i--)
				if (element.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/** index番のelementを消去 */
	public E remove(int index) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			Object tmp = elementData[index];
			/** index番以降のelementを1つ上に繰り上げる */
			for (int i = index; i < this.length - 1; i++)
				elementData[i] = elementData[i + 1];
			/** 最後の要素をnullにしてlength-1 */
			elementData[--this.length] = null;
			return (E) tmp;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** index番にelementをset */
	public E set(int index, E element) {
		try {
			if (index < 0 || index >= this.length)
				throw new IndexOutOfBoundsException();
			Object tmp = elementData[index];
			elementData[index] = element;
			return (E) tmp;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int size() {
		return this.length;
	}

	/** 容量を増やす処理 */
	private void ensureCapacity() {
		if (elementData[elementData.length - 1] != null) {
			Object tmp[] = new Object[this.length + 1];
			System.arraycopy(elementData, 0, tmp, 0, elementData.length);
			elementData = new Object[tmp.length];
			System.arraycopy(tmp, 0, elementData, 0, tmp.length);
		}
	}
}
