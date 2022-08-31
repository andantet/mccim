package mcc.tetris;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public interface TetrisUtil {
    static List<TetrisPiece.Node> loop(String... lines) {
        List<TetrisPiece.Node> nodes = new ArrayList<>();

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                if (chars[x] != ' ') nodes.add(new TetrisPiece.Node(x, y));
            }
        }

        return ImmutableList.copyOf(nodes);
    }
}
