import java.awt.Dimension;

import javax.swing.JFrame;

public class MineWalker {
	public static void main(String args[])
	{
		JFrame frame = new JFrame("MineWalker");
		frame.setPreferredSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MineWalkerPanel().getPanel());
		frame.pack();
		frame.setVisible(true);
	}
	
}