package mcc.tetris;

import net.minecraft.util.Formatting;

import java.util.List;

public class TetrisPiece {
    public static final TetrisPiece PIECE_I = of(Formatting.RED,
        Variant.of(
            " #  ",
            " #  ",
            " #  ",
            " #  "
        ),
        Variant.of(
            "    ",
            "####",
            "    ",
            "    "
        ),
        Variant.of(
            "  # ",
            "  # ",
            "  # ",
            "  # "
        ),
        Variant.of(
            "    ",
            "    ",
            "####",
            "    "
        )
    );
    public static final TetrisPiece PIECE_J = of(Formatting.BLUE,
        Variant.of(
            " #  ",
            " #  ",
            "##  ",
            "    "
        ),
        Variant.of(
            "#   ",
            "### ",
            "    ",
            "    "
        ),
        Variant.of(
            " ## ",
            " #  ",
            " #  ",
            "    "
        ),
        Variant.of(
            "    ",
            "### ",
            "  # ",
            "    "
        )
    );
    public static final TetrisPiece PIECE_L = of(Formatting.GOLD,
        Variant.of(
            " #  ",
            " #  ",
            " ## ",
            "    "
        ),
        Variant.of(
            "    ",
            "### ",
            "#   ",
            "    "
        ),
        Variant.of(
            "##  ",
            " #  ",
            " #  ",
            "    "
        ),
        Variant.of(
            "  # ",
            "### ",
            "    ",
            "    "
        )
    );
    public static final TetrisPiece PIECE_O = of(Formatting.YELLOW,
        Variant.of(
            "##  ",
            "##  ",
            "    ",
            "    "
        )
    );
    public static final TetrisPiece PIECE_S = of(Formatting.LIGHT_PURPLE,
        Variant.of(
            "    ",
            " ## ",
            "##  ",
            "    "
        ),
        Variant.of(
            "#   ",
            "##  ",
            " #  ",
            "    "
        ),
        Variant.of(
            " ## ",
            "##  ",
            "    ",
            "    "
        ),
        Variant.of(
            " #  ",
            " ## ",
            "  # ",
            "    "
        )
    );
    public static final TetrisPiece PIECE_T = of(Formatting.AQUA,
        Variant.of(
            "    ",
            "### ",
            " #  ",
            "    "
        ),
        Variant.of(
            " #  ",
            "##  ",
            " #  ",
            "    "
        ),
        Variant.of(
            " #  ",
            "### ",
            "    ",
            "    "
        ),
        Variant.of(
            " #  ",
            " ## ",
            " #  ",
            "    "
        )
    );
    public static final TetrisPiece PIECE_Z = of(Formatting.GREEN,
        Variant.of(
            "    ",
            "##  ",
            " ## ",
            "    "
        ),
        Variant.of(
            " #  ",
            "##  ",
            "#   ",
            "    "
        ),
        Variant.of(
            "##  ",
            " ## ",
            "    ",
            "    "
        ),
        Variant.of(
            "  # ",
            " ## ",
            " #  ",
            "    "
        )
    );

    public static final List<TetrisPiece> PIECES = List.of(PIECE_I, PIECE_J, PIECE_L, PIECE_O, PIECE_S, PIECE_T, PIECE_Z);

    private final List<Variant> variants;
    private final Formatting color;

    public TetrisPiece(Formatting color, Variant... variants) {
        this.variants = List.of(variants);

        if (!color.isColor()) {
            throw new IllegalArgumentException("Formatting is not a color");
        }

        this.color = color;
    }

    public static TetrisPiece of(Formatting color, Variant... variants) {
        return new TetrisPiece(color, variants);
    }

    public Formatting getColor() {
        return this.color;
    }

    public List<Variant> getVariants() {
        return this.variants;
    }

    public record Variant(Node minNode, Node maxNode, Node... nodes) {
        public static Variant of(String... lines) {
            List<Node> nodes = TetrisUtil.loop(lines);

            if (nodes.size() != 4) {
                throw new IllegalArgumentException("Given lines did not create 4 nodes");
            }

            int minX = nodes.stream().mapToInt(Node::x).min().orElseThrow();
            int minY = nodes.stream().mapToInt(Node::y).min().orElseThrow();
            int maxX = nodes.stream().mapToInt(Node::x).max().orElseThrow();
            int maxY = nodes.stream().mapToInt(Node::y).max().orElseThrow();

            return new Variant(new Node(minX, minY), new Node(maxX, maxY), nodes.toArray(Node[]::new));
        }

        public int getWidth() {
            return (this.maxNode.x - this.minNode.x) + 1;
        }

        public int getHeight() {
            return (this.maxNode.y - this.minNode.y) + 1;
        }
    }

    /**
     * A point between (0, 0) and (3, 3).
     */
    public record Node(int x, int y) {
        public Node(int x, int y) {
            this.x = x;
            this.y = y;

            if (this.x < 0 || this.x > 3) {
                throw new IllegalArgumentException();
            }
            if (this.y < 0 || this.y > 3) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            var that = (Node) obj;
            return this.x == that.x && this.y == that.y;
        }

        @Override
        public String toString() {
            return "Node[" + "x=" + x + ", " + "y=" + y + ']';
        }

    }
}
