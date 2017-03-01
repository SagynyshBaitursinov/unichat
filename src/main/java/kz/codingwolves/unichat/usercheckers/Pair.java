package kz.codingwolves.unichat.usercheckers;

public class Pair {
	
	private final String left;
	private final String right;
	
	public Pair(String left, String right) {
		this.left = left;
		this.right = right;
	}
	
	public String getLeft() { return left; }
	public String getRight() { return right; }
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) return false;
		Pair pairo = (Pair) o;
		return (this.left.equals(pairo.getLeft()) &&
		       this.right.equals(pairo.getRight())) || (this.left.equals(pairo.getRight()) && this.right.equals(pairo.getLeft()));
	}
	
	@Override
	public String toString() {
		return "{" + left + " and " + right + "}";
	}
}
