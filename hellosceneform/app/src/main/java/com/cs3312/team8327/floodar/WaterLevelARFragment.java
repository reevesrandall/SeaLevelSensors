package com.cs3312.team8327.floodar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cs3312.team8327.R;
import com.cs3312.team8327.floodar.Util.HeightFormatter;
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
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.rendering.ViewSizer;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class for handling the AR water level fragment shown in the AR Activity
 */
public class WaterLevelARFragment extends ArFragment implements ViewSizer {

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
        // this.waterLabel =waterLabel;

        if (waterLabel != null) {
            /* waterLabel.setText("Oh hey");

            waterLabel.setBackgroundColor(0xFFF);
            waterLabel.setTextColor(0x0);
            waterLabel.setPadding(10, 10, 10, 10);
            waterLabel.setTextSize(30); */

            // waterLabel.setText(HeightFormatter.stringForHeight(waterHeight));


        }

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Color waterColor = new Color(9.0f / 255.0f, 176.0f / 255.0f, 247.0f / 255.0f, 0.8f);
        MaterialFactory.makeTransparentWithColor(getContext(), waterColor)
                .thenAccept(material -> { this.waterMaterial = material; });

        ModelRenderable.builder()
                .setSource(getContext(), R.raw.andy)
                .build()
                .thenAccept(renderable -> this.waterMaterial = renderable.getMaterial());


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
            Vector3 size = new Vector3(plane.getExtentX(), 0, plane.getExtentZ());
            float[] translation = pose.getTranslation();
            Vector3 center = new Vector3(translation[0], translation[1], translation[2]);
            center.y += waterHeight;
            ModelRenderable water = ShapeFactory.makeCube(size, center, waterMaterial);
            node.setRenderable(water);
        }

        if (planes.size() == 0) { return; }

        if (waterLabel == null && getArSceneView() != null) {
            ViewRenderable.builder()
                    .setView(getContext(), R.layout.water_height_label)
                    .setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER)
                    .setHorizontalAlignment(ViewRenderable.HorizontalAlignment.CENTER)
                    // .setSizer(this)
                    .build()
                    .thenAccept(labelRenderable -> {
                        labelNode = new Node();
                        labelNode.setRenderable(labelRenderable);
//                        scene.addChild(labelNode);
                        labelNode.setParent(scene);

                        waterLabel = (TextView)labelRenderable.getView();
                    });
        }

        if (labelNode != null && planes.size() > 0) {
            waterLabel.setText(HeightFormatter.stringForHeight(waterHeight));

            Plane plane = planes.get(0);
            float[] translation = plane.getCenterPose().getTranslation();
            Vector3 position = new Vector3(translation[0], translation[1] + waterHeight + 0.2f, translation[2]);
            labelNode.setWorldPosition(position);

            // Point at camera:
            /* Vector3 cameraPosition = scene.getCamera().getWorldPosition();
            Vector3 direction = Vector3.subtract(cameraPosition, position);
            Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
            labelNode.setWorldRotation(lookRotation); */
        }

    }

    @Override
    public Vector3 getSize(View view) {
        return new Vector3(30, 15, 0);
    }
}
