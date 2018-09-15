package ca.corefacility.gview.map.gui;

/**
 * Classes that implement this interface are expected to be able to provide notifications about progress,
 * including the amount of progress and progress messages.
 */
public interface ProgressNotifier
{
	public void setMessage(String message);
	public void setProgress(int progress);
}
