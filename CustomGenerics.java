class A<T>
{
	public void add(T a, T b)
	{
		System.out.println("Generic Logic....");
	}
	
}

public class CustomGenerics
{
	public static void main(String[] args)
	{
		A<String> a1 = new A<String>();
		a1.add("good", "morning");

		A<Integer> a1 = new A<Integer>();
		a1.add(11, 22);

		A<Double> a1 = new A<Double>();
		a1.add(1.1, 2.2);
	}
}