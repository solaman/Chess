package main.ChessGUIFiles;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.chessPieces.Bishop;
import main.chessPieces.King;
import main.chessPieces.Knight;
import main.chessPieces.Pawn;
import main.chessPieces.Queen;
import main.chessPieces.Rook;

/**
 * class used to facilitate acquiring of piece
 * representations (images, unicode or otherwise). 
 * the representation of a piece is isolated from the ChessPiece class
 * so that, if desired, various images can be used to
 * represent a single ChessPiece without confounding the ChessPiece classes.
 * 
 * base class uses unicode for representations
 * @author Solaman
 *
 */
public class ChessPieceRepresentations {

	
	private static int[] WHITEPIXEL={255, 255, 255};
	private static int[] BLACKPIXEL={0,0,0};
	
	
	public ChessPieceRepresentations() {
		// TODO Auto-generated constructor stub
	}
	
	public BufferedImage getRepresentation(King king) throws IOException{
		if( king.getPlayer() == 0)
			 return coloredImage("king.png", WHITEPIXEL);
		return coloredImage("king.png", BLACKPIXEL);
	}
	
	public BufferedImage getRepresentation(Queen queen) throws IOException{
		if(queen.getPlayer() ==0)
			return coloredImage("queen.png", WHITEPIXEL);
		return coloredImage("queen.png", BLACKPIXEL);
	}
	
	public BufferedImage getRepresentation(Rook rook) throws IOException{
		if(rook.getPlayer() == 0)
			return coloredImage("rook.png", WHITEPIXEL);
		return coloredImage("rook.png", BLACKPIXEL);
	}
	
	public BufferedImage getRepresentation(Bishop bishop) throws IOException{
		if(bishop.getPlayer() == 0)
			return coloredImage("bishop.png", WHITEPIXEL);
		return coloredImage("bishop.png", BLACKPIXEL);
	}
	
	public BufferedImage getRepresentation(Knight knight) throws IOException{
		if(knight.getPlayer() == 0)
			return coloredImage("knight.png", WHITEPIXEL);
		return coloredImage("knight.png", BLACKPIXEL);
	}
	
	public BufferedImage getRepresentation(Pawn pawn) throws IOException{
		if( pawn.getPlayer() == 0)
			return coloredImage("pawn.png", WHITEPIXEL);
		return coloredImage("pawn.png", BLACKPIXEL);
	}

	private static BufferedImage coloredImage(String fileName, int[] fillPixel) throws IOException {
		BufferedImage image = ImageIO.read(new File(ChessPieceRepresentations.class.getResource("/images/"+fileName).getPath()));
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                if( (pixels[0] + pixels[1] + pixels[0]) > 450){
                	pixels[0] = fillPixel[0];
                	pixels[1] = fillPixel[1];
                	pixels[2] = fillPixel[2];
                	raster.setPixel(xx, yy, pixels);
                }
            }
        }
        return image;
    }

}
