package xyz.udalny.vnomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import xyz.udalny.vnolib.command.servercommands.MSCommand;
import xyz.udalny.vnolib.command.servercommands.enums.MessageColor;
import xyz.udalny.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnomobile.R;

import xyz.udalny.vnomobile.render.Engine;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.ResourceHandler;

import java.io.IOException;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestRendererActivity extends AppCompatActivity {

    private static final String LOREM_IPSUM =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                    " Sed iaculis eget ligula vel ornare. Proin ultricies vel nulla nec" +
                    " imperdiet. Nam vel volutpat arcu. Donec ultricies, eros a pretium egestas, " +
                    "eros orci dapibus mauris, in volutpat sem magna sed nisl. Aenean lacinia, " +
                    "nunc quis gravida sollicitudin, dui turpis rhoncus lacus, vitae dictum nibh " +
                    "augue pretium ante. Aenean vulputate sapien ac molestie pretium. " +
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames " +
                    "ac turpis egestas. Donec feugiat eros sit amet tempus laoreet. Pellentesque " +
                    "vitae augue cursus, pharetra mi in, lacinia magna. Suspendisse ullamcorper " +
                    "at dui in dapibus. Vivamus placerat tortor non maximus lacinia. Nullam " +
                    "faucibus pharetra elit eu ullamcorper. Maecenas venenatis est ante, quis " +
                    "blandit est molestie sit amet. ";

    private SurfaceView surfaceView;
    private Button button;
    private Engine engine;
    private DataDirectory dataDirectory;

    private MSCommand[] msCommands = {

            MSCommand.builder()
                    .characterName("Jill")
                    .spriteName("Normal")
                    .message(LOREM_IPSUM)
                    .boxName("char")
                    .messageColor(MessageColor.WHITE)
                    .characterId(1)
                    .backgroundImageName("vhod")
                    .position(SpritePosition.CENTER)
                    .build(),

            MSCommand.builder()
                    .characterName("Gillian")
                    .spriteName("Weeell")
                    .message("*вздох*")
                    .boxName("$ALT")
                    .messageColor(MessageColor.GREEN)
                    .characterId(2)
                    .backgroundImageName("vhod")
                    .position(SpritePosition.LEFT)
                    .build(),

            MSCommand.builder()
                    .characterName("Donovan")
                    .spriteName("Happy")
                    .message("Дарова. Плесни пива.")
                    .boxName("Красный череп")
                    .messageColor(MessageColor.WHITE)
                    .characterId(6)
                    .backgroundImageName("vhod")
                    .position(SpritePosition.RIGHT)
                    .build(),

            MSCommand.builder()
                    .characterName("Dorothy")
                    .spriteName("Awww")
                    .message("         Каааакаааая         жее       яяяя          хооооорни...         ")
                    .boxName("$ALT")
                    .messageColor(MessageColor.WHITE)
                    .characterId(6)
                    .backgroundImageName("vhod")
                    .position(SpritePosition.RIGHT)
                    .build()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_renderer);
        button = findViewById(R.id.test_renderer_button);
        surfaceView = findViewById(R.id.test_renderer_surface_view);
        dataDirectory = ResourceHandler.getInstance().getDirectory();

        try {
            String designName = dataDirectory.getSettings().get("DesignStyle", "design");
            engine = new Engine(surfaceView, dataDirectory, dataDirectory.getDesign(designName));
        } catch (IOException e) {
            log.error("TestRendererActivity: ", e);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.handle(msCommands[new Random().nextInt(msCommands.length)]);
            }
        });
    }


}