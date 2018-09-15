package ca.corefacility.gview.layout.sequence.circular;

/**
 * Used so we can return the 2,1,or 0 solutions of findDisplacementToIntersection.
 * @author Aaron Petkau
 *
 */
public class DisplacementSolution
{
	private double solution1;
	private double solution2;
	
	public DisplacementSolution(double solution1, double solution2)
	{
		this.solution1 = solution1;
		this.solution2 = solution2;
	}

	public double getSolution1()
	{
		return solution1;
	}

	public double getSolution2()
	{
		return solution2;
	}
	
	@Override
	public String toString()
	{
		return "DisplacementSolution [solution1=" + solution1 + ", solution2="
				+ solution2 + "]";
	}
}
