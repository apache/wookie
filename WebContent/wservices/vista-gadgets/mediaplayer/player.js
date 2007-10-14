function loadSettings() {
    var shuffleSetting = System.Gadget.Settings.read("shuffle");
    var repeatSetting = System.Gadget.Settings.read("repeat");
    var autoStartSetting = System.Gadget.Settings.read("autoStart");
    var volumeSetting = System.Gadget.Settings.read("volume");
    var muteSetting = System.Gadget.Settings.read("mute");

    player.settings.setMode( "shuffle", shuffleSetting == "" || shuffleSetting == "true" );
    player.settings.setMode( "loop", repeatSetting == "" || repeatSetting == "true" );
    player.settings.autoStart = autoStartSetting == "" || autoStartSetting == "true";
    player.settings.volume = volumeSetting == "" ? 50 : volumeSetting;
    player.settings.mute = muteSetting == "true";
}
