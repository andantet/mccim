package mcc.tetris;

import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class TetrisWorld {
    private final List<TetrisPieceInstance> pieces = new ArrayList<>();

    public TetrisWorld() {
    }

    public List<TetrisPieceInstance> getPieces() {
        return this.pieces;
    }

    public TetrisPieceInstance addPiece(Random random) {
        return this.addPiece(TetrisPiece.PIECES.get(random.nextInt(TetrisPiece.PIECES.size())), random);
    }

    public TetrisPieceInstance addPiece(TetrisPiece piece, Random random) {
        List<TetrisPiece.Variant> variants = piece.getVariants();
        TetrisPiece.Variant variant = variants.get(random.nextInt(variants.size()));
        TetrisPieceInstance instance = new TetrisPieceInstance(this, piece, variant, this.getMaxX() / 2, this.getMaxY());
        this.pieces.add(0, instance);
        return instance;
    }

    public int getMaxX() {
        return 9;
    }

    public int getMaxY() {
        return 15;
    }
}
