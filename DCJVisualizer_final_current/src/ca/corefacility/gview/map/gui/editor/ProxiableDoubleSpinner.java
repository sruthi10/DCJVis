package ca.corefacility.gview.map.gui.editor;

import java.text.ParseException;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

import ca.corefacility.gview.utils.Util;

public class ProxiableDoubleSpinner extends JSpinner
{
	public static final String PROXY = "*";

	private static final long serialVersionUID = 1L;

	public static ProxiableDoubleSpinner createSpinner(double value, double minimum, double maximum, double stepSize,
			int precision)
	{
		ProxiableSpinnerDoubleModel model = new ProxiableSpinnerDoubleModel(value, minimum, maximum, stepSize,
				precision);

		return new ProxiableDoubleSpinner(model, precision);
	}

	public void setProxy()
	{
		((ProxiableSpinnerDoubleModel) this.getModel()).setProxy();
	}

	private ProxiableDoubleSpinner(ProxiableSpinnerDoubleModel model, int precision)
	{
		super(model);

		JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) this.getEditor();
		JFormattedTextField tf = editor.getTextField();

		tf.setHorizontalAlignment(JTextField.RIGHT);
		tf.setFormatterFactory(new ProxyFormatterFactory());

		((DefaultEditor) this.getEditor()).getTextField().setEditable(true);
	}

	private static class ProxiableSpinnerDoubleModel extends AbstractSpinnerModel
	{
		private final double minimum;
		private final double maximum;
		private final double stepSize;
		private final int precision;

		private Object value;

		public ProxiableSpinnerDoubleModel(double value, double minimum, double maximum, double stepSize, int precision)
		{
			this.minimum = minimum;
			this.maximum = maximum;
			this.stepSize = stepSize;
			this.precision = precision;

			this.value = value;
		}

		@Override
		public Object getNextValue()
		{
			if (this.value.equals(PROXY) || this.value == null)
			{
				return this.minimum;
			}
			else if ((Double) value >= this.maximum)
			{
				return this.maximum;
			}
			else if ((Double) value < this.minimum)
			{
				return this.minimum;
			}
			else
			{
				return (Double) Math.min(
						(Math.round(((Double) this.value + this.stepSize) * Math.pow(10, precision)) / Math.pow(10,
								precision)), this.maximum);
			}
		}

		@Override
		public Object getPreviousValue()
		{
			if (this.value.equals(PROXY) || this.value == null)
			{
				return this.minimum;
			}
			else if ((Double) value > this.maximum)
			{
				return this.maximum;
			}
			else if ((Double) value <= this.minimum)
			{
				return this.minimum;
			}
			else
			{
				return (Double) Math.max(
						(Math.round(((Double) this.value - this.stepSize) * Math.pow(10, precision)) / Math.pow(10,
								precision)), this.minimum);
			}
		}

		public void setProxy()
		{
			this.value = PROXY;
			fireStateChanged();
		}

		@Override
		public void setValue(Object value)
		{
			Double d;

			if (PROXY.equals(value))
			{
				fireStateChanged();
				return;
			}
			// Double:
			else if (value instanceof Double)
			{
				d = (Double) value;
			}
			// Convert Integer to Double:
			else if (value instanceof Integer)
			{
				d = ((Integer) value).doubleValue();
			}
			// Convert String to Double:
			else if (value instanceof String && Util.isDouble((String) value))
			{
				d = Double.parseDouble((String) value);
			}
			// Invalid:
			else
			{
				throw new IllegalArgumentException("Invalid spinner value.");
			}

			if (!(d.equals(this.value)))
			{
				if (d >= this.minimum && d <= this.maximum)
				{
					this.value = d;
				}

				fireStateChanged();
			}
		}

		@Override
		public Object getValue()
		{
			return this.value;
		}
	}

	private static class ProxyFormatterFactory extends DefaultFormatterFactory
	{
		private static final long serialVersionUID = 1L;

		@Override
		public AbstractFormatter getDefaultFormatter()
		{
			return new ProxyFormatter();
		}
	}

	private static class ProxyFormatter extends DefaultFormatter
	{
		private static final long serialVersionUID = 1L;

		public ProxyFormatter()
		{
			super();
		}

		@Override
		public Object stringToValue(String text) throws ParseException
		{
			return text;
		}

		@Override
		public String valueToString(Object value) throws ParseException
		{
			if (value != null && value.equals(PROXY))
			{
				return PROXY;
			}
			else if (value instanceof Double)
			{
				return Double.toString((Double) value);
			}
			else
			{
				return value.toString();
			}
		}
	}
}
