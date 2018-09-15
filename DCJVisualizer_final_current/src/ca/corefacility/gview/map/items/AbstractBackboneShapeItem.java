package ca.corefacility.gview.map.items;

import java.awt.Paint;


// TODO define how bounds are set, since this needs to be defined in a PNode
public abstract class AbstractBackboneShapeItem extends AbstractShapeItem implements BackboneShapeItem // event listener so we can keep bounds and shape in sync
{
	private static final long serialVersionUID = -1254104030105706501L;

	@Override
	public void setPaint( Paint paint )
	{
		super.setPaint( paint );
		this.normalPaint = paint;
	}
}
