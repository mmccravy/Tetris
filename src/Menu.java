/**
 * All aspects of the panel not involved in the actual game play.
 * Next Piece, Score, Current Level, Game Controls
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Menu extends JPanel{
	private playGame tetris;
	private static final long serialVersionUID = 1L;
	//npp = next piece preview
	private static final int nppCenterX = 150;
	private static final int nppCenterY = 65;
	private static final int nppTileSize = GameBoard.tileSize >> 1;
	private static final int nppTileNumber = 5;
	private static final int nppIndent = GameBoard.indent >> 1;
	private static final int nppSquare = (nppTileSize * nppTileNumber >> 1);
	
	private static final int smallSpacer = 5;		
	private static final int largeSpacer = 25;		
	
	private static final int statsLoc = 175;
	private static final int controlsLoc = 300;	
	
	private static final Font regular = new Font("Fixedsys", Font.BOLD, 12);
	private static final Font large = new Font("Fixedsys", Font.BOLD, 19);
	/**
	 * Creates a menu panel
	 * @param tetris -- tetris instance
	 */
	public Menu(playGame tetris) {
		this.tetris = tetris;
		setPreferredSize(new Dimension(200, GameBoard.boardHeight));
		setBackground(Color.DARK_GRAY);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		int offset;
		
		g.setFont(large);
		g.drawString("Stats", smallSpacer, offset = statsLoc);
		g.setFont(regular);
		g.drawString("Level: " + tetris.getLevel(), smallSpacer, offset += largeSpacer);
		g.drawString("Score: " + tetris.getScore(), smallSpacer, offset += largeSpacer);
		g.setFont(large);
		g.drawString("Controls", smallSpacer, offset = controlsLoc);
		g.setFont(regular);
		g.drawString("Move Left -- A / Left Arrow", smallSpacer, offset += largeSpacer);
		g.drawString("Move Right -- D / Right Arrow", smallSpacer, offset += largeSpacer);
		g.drawString("Drop Piece -- S / Down Arrow", smallSpacer, offset += largeSpacer);
		g.drawString("Rotate(left and right) -- Q and E", smallSpacer, offset += largeSpacer);
		g.drawString("Pause Game -- P", smallSpacer, offset += largeSpacer);
		g.setFont(large);
		g.drawString("Next Piece:", smallSpacer, 70);
		g.drawRect(nppCenterX - nppSquare, nppCenterY - nppSquare, nppSquare * 2, nppSquare * 2);
		
		Piece type = tetris.getNextPiece();
		if(!tetris.hasGameEnded() && type != null)
		{
			int c = type.getNumberOfColumns();
			int r = type.getNumberOfRows();
			int dim = type.getDimension();
			
			int xBeginning = (nppCenterX - (c * nppTileSize / 2));
			int yBeginning = (nppCenterY - (r * nppTileSize / 2));
			int top = type.getSpaceTop(0);
			int left = type.getSpaceLeft(0);

			for(int i = 0; i < dim; i++)
			{
				for(int j = 0; j < dim; j++)
				{
					if(type.isTilePresent(j, i, 0))
						drawTile(type, xBeginning + ((j - left) * nppTileSize), yBeginning + ((i - top) * nppTileSize), g);
				}
			}
		}
	}	
	/**
	 * Draws a tile in the preview section
	 * @param type -- type of tile
	 * @param x -- x coordinate
	 * @param y -- y coordinate
	 * @param g -- graphics object
	 */
	private void drawTile(Piece type, int x, int y, Graphics g){
		g.setColor(type.getColor());
		g.fillRect(x, y, nppTileSize, nppTileSize);
		g.setColor(type.getDarker());
		g.fillRect(x, y + nppTileSize - nppIndent, nppTileSize, nppIndent);
		g.fillRect(x + nppTileSize - nppIndent, y, nppIndent, nppTileSize);
		g.setColor(type.getLighter());
		
		for(int i = 0; i < nppIndent; i++)
		{
			g.drawLine(x, y + i, x + nppTileSize - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + nppTileSize - i - 1);
		}
	}
	
}
