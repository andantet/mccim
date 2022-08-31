package mcc.tetris;

import com.mojang.blaze3d.systems.RenderSystem;
import mcc.tetris.TetrisPiece.Node;
import mcc.tetris.TetrisPiece.Variant;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

import java.util.ArrayList;
import java.util.List;

public class TetrisPieceInstance extends DrawableHelper {
    private final TetrisWorld world;
    private final TetrisPiece piece;
    private Variant variant;

    private int x, y;

    public TetrisPieceInstance(TetrisWorld world, TetrisPiece piece, Variant variant, int x, int y) {
        this.world = world;
        this.piece = piece;
        this.variant = variant;
        this.x = x;
        this.y = y;
    }

    public boolean move(Direction direction) {
        if (direction == null) return false;

        Axis axis = direction.getAxis();
        if (axis == Axis.X) {
            int x = direction.getVector().getX();
            int nux = this.x + x;
            if (this.doesNotCollideAt(nux, this.y) && this.isOnGridWhen(nux, this.y)) {
                this.x = nux;
            }
        } else if (axis == Axis.Y) {
            int y = direction.getVector().getY();
            int nuy = this.y + y;
            if (this.doesNotCollideAt(this.x, nuy) && this.isOnGridWhen(this.x, nuy)) {
                this.y = nuy;
            }
        }

        return false;
    }

    public boolean collidesWith(Variant variant, int x, int y) {
        for (Node node : variant.nodes()) {
            int nx = this.x + node.x();
            if (nx == x) return true;
            int ny = this.y + node.y();
            if (ny == y) return true;
        }

        return false;
    }

    public boolean collidesWith(int x, int y) {
        return this.collidesWith(this.variant, x, y);
    }

    public boolean doesNotCollideAt(int x, int y) {
        List<TetrisPieceInstance> pieces = new ArrayList<>(this.world.getPieces());
        pieces.remove(this);

        for (TetrisPieceInstance piece : pieces) {
            if (piece.collidesWith(x, y)) return false;
        }

        return true;
    }

    public boolean isOnGridWhen(Variant variant, int x, int y) {
        int maxX = this.world.getMaxX();
        int maxY = this.world.getMaxY();

        for (Node node : variant.nodes()) {
            int nx = x + node.x();
            if (nx < 0 || nx > maxX) return false;
            int ny = y + node.y();
            if (ny < 0 || ny > maxY) return false;
        }

        return true;
    }

    public boolean isOnGridWhen(int x, int y) {
        return this.isOnGridWhen(this.variant, x, y);
    }

    public boolean isOnGridWhen(Variant variant) {
        return this.isOnGridWhen(variant, this.x, this.y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float[] getColor() {
        int colorValue = this.piece.getColor().getColorValue();
        float r = ((colorValue >> 16) & 0xFF) / 255.0f;
        float g = ((colorValue >> 8) & 0xFF) / 255.0f;
        float b = ((colorValue) & 0xFF) / 255.0f;
        return new float[]{ r, g, b };
    }

    public void render(MatrixStack matrices, int size, int x, int y) {
        float[] color = this.getColor();
        RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);

        int renderX = x + (this.x * size);
        int renderY = y - (this.y * size);

        for (Node node : this.variant.nodes()) {
            drawTexture(matrices,
                renderX + (node.x() * size),
                renderY + ((node.y() - 4) * size),
                0, 0, size, size, size, size
            );
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        if (this == TetrisScreen.INSTANCE.getActivePiece()) {
            int endRenderX = renderX + (4 * size);
            int endRenderY = renderY - (4 * size);
            fill(matrices, renderX, renderY, renderX + 1, renderY - 1, 0xFFFF0000);
            fill(matrices, endRenderX, endRenderY, endRenderX + 1, endRenderY - 1, 0xFFFF0000);
        }
    }

    public Variant cycleVariant() {
        List<Variant> variants = this.piece.getVariants();
        int current = variants.indexOf(this.variant);
        return variants.get((current + 1) % variants.size());
    }

    public Variant getVariant() {
        return this.variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }
}
