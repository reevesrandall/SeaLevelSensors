package com.google.ar.sceneform.samples.hellosceneform;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WaterLevelARFragment extends ArFragment {

    // private Map<Plane, Node> waterNodes = new HashMap<>();
    private List<Node> waterNodes = new ArrayList<>();
    private Material waterMaterial;

    private float waterHeight = 1.0f;

    public float getWaterHeight() {
        return waterHeight;
    }

    public void setWaterHeight(float waterHeight) {
        this.waterHeight = waterHeight;
    }

    public TextView waterLabel;
    private Node labelNode;

    public void setWaterLabel(TextView waterLabel) {
        this.waterLabel = waterLabel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Color waterColor = new Color(9.0f / 255.0f, 176.0f / 255.0f, 247.0f / 255.0f, 0.8f);
        MaterialFactory.makeTransparentWithColor(getContext(), waterColor)
                .thenAccept(material -> { this.waterMaterial = material; });

        waterLabel = new TextView(getContext());
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        if (waterMaterial == null) {
            return;
        }

        Session session = getArSceneView().getSession();
        List<Plane> planes = session.getAllTrackables(Plane.class).stream().collect(Collectors.toList());

        Scene scene = getArSceneView().getScene();
        for (Node node: waterNodes) {
            scene.removeChild(node);
        }
        waterNodes.clear();

        for (Plane plane: planes) {
            Node node = new Node();
            node.setParent(scene);
            waterNodes.add(node);

            Pose pose = plane.getCenterPose();
            float width = 0.8f; // meters
            Vector3 size = new Vector3(width, waterHeight, width);
            float[] translation = pose.getTranslation();
            Vector3 center = new Vector3(translation[0], translation[1], translation[2]);
            center.y += waterHeight / 2.0f;
            ModelRenderable water = ShapeFactory.makeCube(size, center, waterMaterial);
            node.setRenderable(water);
        }

        /* if (waterLabel != null && planes.size() > 0) {
            waterLabel.setText(HeightFormatter.stringForHeight(waterHeight));

            Plane plane = planes.get(0);
            float[] translation = plane.getCenterPose().getTranslation();
            Vector3 position = new Vector3(translation[0], translation[1] + waterHeight, translation[2]);
            ViewRenderable.builder()
                    .setView(getContext(), waterLabel)
                    .build()
                    .thenAccept(labelRenderable -> {
                        if (labelNode != null) {
                            scene.removeChild(labelNode);
                        }

                        labelNode = new AnchorNode();
                        labelNode.setWorldPosition(position);
                        labelNode.setRenderable(labelRenderable);
                        scene.addChild(labelNode);
                    });
        } */

    }
}
