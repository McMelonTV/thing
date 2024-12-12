package com.nodiumhosting.reactui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;

public class WebViewScreen extends Screen {
    private JFXPanel jfxPanel;
    private WebView webView;

    public WebViewScreen() {
        super(Text.literal("WebView Renderer"));

        // Initialize JavaFX toolkit if it's not already done.
        if (!Platform.isFxApplicationThread()) {
            // Ensure JavaFX is initialized on the correct thread.
            Platform.runLater(() -> {
                // Now we initialize JavaFX components once the platform is running.
                initializeJavaFX();
            });
        } else {
            initializeJavaFX();
        }
    }

    private void initializeJavaFX() {
        // Initialize JavaFX components
        jfxPanel = new JFXPanel();
        webView = new WebView();
        webView.getEngine().load("https://example.com");

        // Set the WebView size and place it on the JFXPanel
        Scene scene = new Scene(webView, 800, 600);
        jfxPanel.setScene(scene);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Make sure to run OpenGL rendering in the correct thread
        if (context != null) {
            renderWebViewToOpenGL(context);
        }
    }

    private void renderWebViewToOpenGL(DrawContext context) {
        //select the current webview frame as a texture



        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.BLIT_SCREEN);
        bufferBuilder.vertex(0, 0, 0).texture(0, 0);
        bufferBuilder.vertex(0, context.getScaledWindowHeight(), 0).texture(0, 1);
        bufferBuilder.vertex(context.getScaledWindowWidth(), context.getScaledWindowHeight(), 0).texture(1, 1);
        bufferBuilder.vertex(context.getScaledWindowWidth(), 0, 0).texture(1, 0);
        BufferRenderer.draw(bufferBuilder.end());
    }

    @Override
    public void close() {
        super.close();

        // Ensure Platform.exit() is called on the JavaFX thread
        Platform.runLater(Platform::exit); // Shutdown JavaFX platform gracefully
    }
}
