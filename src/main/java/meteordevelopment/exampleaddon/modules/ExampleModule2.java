package meteordevelopment.exampleaddon.modules;

import minegame159.meteorclient.modules.Categories;
import minegame159.meteorclient.modules.Module;

public class ExampleModule2 extends Module {
    public ExampleModule2() {
        super(Categories.Player, "example-module-2", "This is an example module that is in an existing category.");
    }
}