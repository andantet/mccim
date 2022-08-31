package mcc.tetris;

import com.mojang.blaze3d.systems.RenderSystem;
import mcc.MCC;
import mcc.tetris.TetrisPiece.Node;
import mcc.tetris.TetrisPiece.Variant;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class TetrisScreen extends Screen {
    public static final TetrisScreen INSTANCE = new TetrisScreen(null);

    public static final int
        BACKGROUND_WIDTH = 176, BACKGROUND_HEIGHT = 184,
        PAUSE_BUTTON_SIZE = 20, PIECE_SIZE = 10;

    public static final Identifier
        PART_TEXTURE = new Identifier("mccim", "textures/gui/tetris/part.png"),
        BACKGROUND_TEXTURE = new Identifier("mccim", "textures/gui/tetris/background.png"),
        PAUSE_BUTTONS_TEXTURE = new Identifier("mccim", "textures/gui/tetris/buttons.png");

    private final Screen parent;
    private final Random random;
    private final RenderTickCounter renderTickCounter = new RenderTickCounter(20, 0L);
    private PausedWidget pausedWidget;
    private int x, y;

    private TetrisWorld tetrisWorld;
    private TetrisPieceInstance activePiece;
    private int tick;

    public TetrisScreen(Screen parent) {
        super(Text.of("Tetris"));
        this.parent = parent;
        this.random = Random.create();

        this.tetrisWorld = new TetrisWorld();
        this.tetrisWorld.addPiece(this.random);
    }

    public TetrisPieceInstance getActivePiece() {
        return this.activePiece;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (112 / 2);
        this.y = (this.height / 2) - (BACKGROUND_HEIGHT / 2);

        boolean paused = this.pausedWidget == null || this.pausedWidget.isPaused();
        this.pausedWidget = new PausedWidget(this.x + 111, this.y + BACKGROUND_HEIGHT - PAUSE_BUTTON_SIZE - 4, PAUSE_BUTTON_SIZE, PAUSE_BUTTON_SIZE);
        this.pausedWidget.paused = paused;
        this.addDrawableChild(this.pausedWidget);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (key == GLFW.GLFW_KEY_R) {
            this.tetrisWorld = new TetrisWorld();
            this.tetrisWorld.addPiece(this.random);
        }

        if (!this.pausedWidget.isPaused()) {
            Direction direction = null;

            if (key == GLFW.GLFW_KEY_RIGHT) {
                direction = Direction.WEST;
            }

            if (key == GLFW.GLFW_KEY_LEFT) {
                direction = direction == null ? Direction.EAST : null;
            }

            if (key == GLFW.GLFW_KEY_DOWN) {
                direction = direction == null ? Direction.DOWN : null;
            }

            if (key == GLFW.GLFW_KEY_UP) {
                this.tryRotate(this.activePiece);
            }

            this.activePiece.move(direction);
        }

        return super.keyPressed(key, scanCode, modifiers);
    }

    public void tryRotate(TetrisPieceInstance piece) {
        Variant variant = piece.cycleVariant();
        if (piece.isOnGridWhen(variant)) {
            piece.setVariant(variant);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        List<TetrisPieceInstance> pieces = this.tetrisWorld.getPieces();
        this.activePiece = pieces.get(0);

        boolean paused = this.pausedWidget.isPaused();
        if (!paused) {
            this.renderBackground(matrices);
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        if (MCC.getConfig().debug.tetrisPieces) {
            RenderSystem.setShaderTexture(0, PART_TEXTURE);
            for (int i = 0; i < TetrisPiece.PIECES.size(); i++) {
                TetrisPiece piece = TetrisPiece.PIECES.get(i);
                Formatting color = piece.getColor();
                int colorValue = color.getColorValue();
                float r = ((colorValue >> 16) & 0xFF) / 255.0f;
                float g = ((colorValue >> 8) & 0xFF) / 255.0f;
                float b = ((colorValue) & 0xFF) / 255.0f;

                int rendered = 0;
                for (Variant variant : piece.getVariants()) {
                    for (Node node : variant.nodes()) {
                        RenderSystem.setShaderColor(r, g, b, 1.0f);

                        drawTexture(matrices,
                            20 + (node.x() * PIECE_SIZE) + (PIECE_SIZE * 5 * i),
                            20 + (node.y() * PIECE_SIZE) + (PIECE_SIZE * 5 * rendered),
                            0, 0, PIECE_SIZE, PIECE_SIZE, PIECE_SIZE, PIECE_SIZE
                        );

                        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    }

                    rendered++;
                }
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            // title
            RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
            drawTexture(matrices, this.x, this.y, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
            this.textRenderer.draw(matrices, this.title, (float) ((this.width / 2) - (this.textRenderer.getWidth(this.title) / 2)), this.y + 6, 0x000000);

            // pieces
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, PART_TEXTURE);

            int maxX = this.tetrisWorld.getMaxX();
            int maxY = this.tetrisWorld.getMaxY();
            int x = this.x + 6;
            int y = this.y + 28 + (maxY * PIECE_SIZE);

            for (TetrisPieceInstance instance : pieces) {
                instance.render(matrices, PIECE_SIZE, x, y);
            }

            fill(matrices, x, y, x + 1, y - 1,0xFF00FF00);
            fill(matrices, x + (maxX * PIECE_SIZE), y - (maxY * PIECE_SIZE), x + (maxX * PIECE_SIZE) + 1, y - (maxY * PIECE_SIZE) - 1,0xFF00FF00);
        }

        if (paused) this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        if (this.pausedWidget.isPaused()) return;

        if (tick % 20 == 0) {
            this.activePiece.move(Direction.DOWN);

            if (this.activePiece.getY() == 0) {
                this.tetrisWorld.addPiece(this.random);
            }
        }

        this.tick++;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
        this.pausedWidget.paused = true;
    }

    public static class PausedWidget extends PressableWidget {
        private boolean paused = true;

        public PausedWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty());
        }

        @Override
        public void onPress() {
            this.paused = !this.paused;
        }

        public boolean isPaused() {
            return this.paused;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
            builder.put(NarrationPart.TITLE, this.getNarrationMessage());
            if (this.active) {
                if (this.isFocused()) {
                    builder.put(NarrationPart.USAGE, Text.translatable("narration.checkbox.usage.focused"));
                } else {
                    builder.put(NarrationPart.USAGE, Text.translatable("narration.checkbox.usage.hovered"));
                }
            }
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderTexture(0, PAUSE_BUTTONS_TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            drawTexture(matrices, this.x, this.y, this.paused ? 20 : 0, this.isFocused() || this.isHovered() ? 20 : 0, 20, this.height, 64, 64);
        }
    }
}
