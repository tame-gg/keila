package gg.tame.palladium.async.path;

import net.minecraft.world.level.pathfinder.NodeEvaluator;

public interface NodeEvaluatorGenerator {
    NodeEvaluator generate(NodeEvaluatorFeatures nodeEvaluatorFeatures);
}
