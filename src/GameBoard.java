/**game grid and actions involved with the playable game board*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GameBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	private playGame tetris;
	private Piece[][] tiles;
	
	public static final int minColorShade = 35;  //light tile shade
	public static final int maxColorShade = 255 - minColorShade;  //dark tile shade
		
	private static final int border = 5;
	public static final int tileSize = 24;
	public static final int indent = 4;
	
	public static final int numOfColums = 10;
	private static final int activeNumRows = 20;
	private static final int passiveNumRows = 2;
	public static final int totalNumRows = activeNumRows + passiveNumRows;
	
	private static final int horizontalAxis = numOfColums * tileSize / 2;
	private static final int verticalAxis = activeNumRows * tileSize / 2;
	
	public static final int boardWidth = numOfColums * tileSize + border * 2;
	public static final int boardHeight = activeNumRows * tileSize + border * 2;
	
	private static final Font titleFont = new Font("Fixedsys", Font.BOLD, 36);
	private static final Font regularFont = new Font("Fixedsys", Font.BOLD, 12);
	
	/**
	 * Sets a tile at the desired intersection(col/row)
	 * @param x -- column
	 * @param y -- row
	 * @param type -- tile value
	 */
	private void setTile(int col, int row, Piece type) {
		tiles[row][col] = type;
	}	
	/**
	 * Gets a tile based on the col/row pair
	 * @param x -- column
	 * @param y -- row
	 * @return -- the desired tile
	 */
	private Piece getTile(int col, int row){
		return tiles[row][col];
	}
	/**
	 * Creates a new game board
	 * @param tetris -- instance of Tetris
	 */
	public GameBoard(playGame tetris){
		this.tetris = tetris;
		this.tiles = new Piece[totalNumRows][numOfColums];
		
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));	
	}
	/**
	 * Wipes the game board
	 */
	public void wipe(){
		for(int i = 0; i < totalNumRows; i++)
		{
			for(int j = 0; j < numOfColums; j++)
			{
				tiles[i][j] = null;
			}
		}
	}
	/**
	 * Removes lines from the board
	 * @return -- number of lines removed
	 */
	public int lineScan(){
		int lineNum = 0;
		
		for(int row = 0; row < totalNumRows; row++)
		{
			if(lineRemove(row))
				lineNum++;
		}
		return lineNum;
	}	
	/**
	 * Checks if the row is full, then removes if so
	 * @param line -- target row
	 * @return -- if full or not
	 */
	private boolean lineRemove(int line){
		for(int col = 0; col < numOfColums; col++)	//if any column is empty, row is not full
		{
			if(!isInhabited(col, line))
				return false;
		}
		
		for(int row = line - 1; row >= 0; row--)	//if it is full, all rows get shifted down 
		{											
			for(int col = 0; col < numOfColums; col++)
			{
				setTile(col, row + 1, getTile(col, row));
			}
		}
		return true;
	}
	/**
	 * Checks if a tile has already been accessed
	 * @param x -- x coordinate
	 * @param y -- y coordinate
	 * @return -- if the tile is valid/invalid
	 */
	private boolean isInhabited(int x, int y){
		return tiles[y][x] != null;
	}
	/**
	 * Using the provided coordinates, checks if a piece can be inserted
	 * @param type -- specific piece
	 * @param x -- x coordinate
	 * @param y -- y coordinate
	 * @param rotation -- The rotation of the piece.
	 * @return valid/invalid position
	 */
	public boolean canPieceInsert(Piece type, int x, int y, int rotation){
		if(x < -type.getSpaceLeft(rotation) ||
				x + type.getDimension() - type.getSpaceRight(rotation) >= numOfColums)	//valid column
			return false;
		
		if(y < -type.getSpaceTop(rotation) ||
				y + type.getDimension() - type.getSpaceBottom(rotation) >= totalNumRows)	//valid row
			return false;
		
		for(int col = 0; col < type.getDimension(); col++)	//comparing tiles for overlap
		{
			for(int row = 0; row < type.getDimension(); row++)
			{
				if(type.isTilePresent(col, row, rotation) && isInhabited(x + col, y + row))
					return false;
			}
		}
		return true;
	}
	/**
	 * Adds a piece to the game board(will overwrite)
	 * @param type -- specific piece
	 * @param x -- x coordinate
	 * @param y -- y coordinate
	 * @param rotation -- The rotation of the piece
	 */
	public void addGamePiece(Piece type, int x, int y, int rotation){
	
		for(int col = 0; col < type.getDimension(); col++)
		{
			for(int row = 0; row < type.getDimension(); row++)
			{
				if(type.isTilePresent(col, row, rotation))
					setTile(col + x, row + y, type);
				
			}
		}
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.translate(border, border);
		
		if(tetris.isPaused())	//different game states
		{
			g.setFont(titleFont);
			g.setColor(Color.LIGHT_GRAY);
			String display = "PAUSED";
			g.drawString(display, horizontalAxis - g.getFontMetrics().stringWidth(display) / 2, verticalAxis);
		}
		else if(tetris.isNewGame() || tetris.hasGameEnded()) //similar states
		{
			g.setFont(titleFont);
			g.setColor(Color.LIGHT_GRAY);
			String display = tetris.isNewGame() ? "TETRIS" : "GAME OVER";
			
			g.drawString(display, horizontalAxis - g.getFontMetrics().stringWidth(display) / 2, 150);
			g.setFont(regularFont);
			
			display = "Press Enter to Play" + (tetris.isNewGame() ? "" : " Again");
			g.drawString(display, horizontalAxis - g.getFontMetrics().stringWidth(display) / 2, 300);
		}
		else
		{
			for(int i = 0; i < numOfColums; i++)
			{
				for(int j = passiveNumRows; j < totalNumRows; j++)
				{
					Piece tile = getTile(i, j);
					if(tile != null)
						drawTile(tile, i * tileSize, (j - passiveNumRows) * tileSize, g);
				}
			}
			
			Piece type = tetris.getPiece();	//make the piece
			int columnPiece = tetris.getColumn();
			int rowPiece = tetris.getRow();
			int rotation = tetris.getRotation();

			for(int col = 0; col < type.getDimension(); col++)
			{
				for(int row = 0; row < type.getDimension(); row++)
				{
					if(rowPiece + row >= 2 && type.isTilePresent(col, row, rotation))
						drawTile(type, (columnPiece + col) * tileSize, (rowPiece + row - passiveNumRows) * tileSize, g);
				}
			}
			
			Color base = type.getColor();	//ghost piece
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
			for(int lowest = rowPiece; lowest < totalNumRows; lowest++)
			{
				if(canPieceInsert(type, columnPiece, lowest, rotation))					
					continue;				
				lowest--;

				for(int col = 0; col < type.getDimension(); col++)
				{
					for(int row = 0; row < type.getDimension(); row++)
					{
						if(lowest + row >= 2 && type.isTilePresent(col, row, rotation))
							drawTile(base, base.brighter(), base.darker(), (columnPiece + col) * tileSize, (lowest + row - passiveNumRows) * tileSize, g);
					}
				}
				break;
			}

			g.setColor(Color.DARK_GRAY);
			
			for(int i = 0; i < numOfColums; i++)
			{
				for(int j = 0; j < activeNumRows; j++)
				{
					g.drawLine(0, j * tileSize, numOfColums * tileSize, j * tileSize);
					g.drawLine(i * tileSize, 0, i * tileSize, activeNumRows * tileSize);
				}
			}
		}

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, tileSize * numOfColums, tileSize * activeNumRows);
	}
	/**
	 * Draws the tile (1)
	 * @param type -- specific piece
	 * @param col -- column.
	 * @param row -- row.
	 * @param g -- graphics object
	 */
	private void drawTile(Piece type, int col, int row, Graphics g){
		drawTile(type.getColor(), type.getLighter(), type.getDarker(), col, row, g);
	}
	/**
	 * Draws the tile (2)
	 * @param base -- base color
	 * @param light -- light color
	 * @param dark -- dark color
	 * @param col -- column
	 * @param row -- row
	 * @param g -- graphics object
	 */
	private void drawTile(Color base, Color light, Color dark, int col, int row, Graphics g) {
		g.setColor(base);
		g.fillRect(col, row, tileSize, tileSize);
		g.setColor(dark);
		g.fillRect(col, row + tileSize - indent, tileSize, indent);
		g.fillRect(col + tileSize - indent, row, indent, tileSize);	//indent shading
		g.setColor(light);
		for(int i = 0; i < indent; i++)
		{
			g.drawLine(col, row + i, col + tileSize - i - 1, row + i);
			g.drawLine(col + i, row, col + i, row + tileSize - i - 1);
		}
	}

}
