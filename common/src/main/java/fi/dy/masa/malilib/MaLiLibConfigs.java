package fi.dy.masa.malilib;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.config.options.table.ConfigTable;
import fi.dy.masa.malilib.config.options.table.Label;
import fi.dy.masa.malilib.config.options.table.TableRow;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.time.DurationFormat;
import fi.dy.masa.malilib.util.time.TimeFormat;

import static fi.dy.masa.malilib.config.options.table.type.EntryTypes.*;

public class MaLiLibConfigs implements IConfigHandler
{
    private static final String CONFIG_FILE_NAME = MaLiLibReference.MOD_ID + ".json";

    private static final String GENERIC_KEY = MaLiLibReference.MOD_ID+".config.generic";
    public static class Generic
    {
        public static final ConfigHotkey            IGNORED_KEYS                = new ConfigHotkey            ("ignoredKeys",      "").apply(GENERIC_KEY);
        public static final ConfigHotkey            OPEN_GUI_CONFIGS            = new ConfigHotkey            ("openGuiConfigs",   "A,C").apply(GENERIC_KEY);
//        public static final ConfigBooleanHotkeyed   ENABLE_ACTIONBAR_MESSAGES   = new ConfigBooleanHotkeyed   ("enableActionbarMessages", true, "").apply(GENERIC_KEY);
        public static final ConfigInteger           ACTIONBAR_HUD_TICKS         = new ConfigInteger           ("actionbarHudTicks",       60, 1, 240).apply(GENERIC_KEY);
        public static final ConfigFloat             IN_GAME_MESSAGE_TIMEOUT     = new ConfigFloat             ("inGameMessageTimeout",    5.0f, 0.5f, 15.0f).apply(GENERIC_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_CONFIG_SWITCHER      = new ConfigBooleanHotkeyed   ("enableConfigSwitcher",    true, "").apply(GENERIC_KEY);
        public static final ConfigBooleanHotkeyed   RENDER_TRANSPARENCY_FIX     = new ConfigBooleanHotkeyed   ("renderTransparencyFix",   true, "").apply(GENERIC_KEY);
        public static final ConfigBoolean           REALMS_COMMON_CONFIG        = new ConfigBoolean           ("realmsCommonConfig",      true).apply(GENERIC_KEY);

        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                IGNORED_KEYS,
                OPEN_GUI_CONFIGS,
//                ENABLE_ACTIONBAR_MESSAGES,
                ACTIONBAR_HUD_TICKS,
                IN_GAME_MESSAGE_TIMEOUT,
                ENABLE_CONFIG_SWITCHER,
                RENDER_TRANSPARENCY_FIX,
                REALMS_COMMON_CONFIG
        );

        // Can't add OPEN_GUI_CONFIGS here, because things will break
        public static final List<IHotkey> HOTKEY_LIST = ImmutableList.of(
                OPEN_GUI_CONFIGS,
//                ENABLE_ACTIONBAR_MESSAGES,
                ENABLE_CONFIG_SWITCHER,
                RENDER_TRANSPARENCY_FIX
        );
    }

    private static final String DEBUG_KEY = MaLiLibReference.MOD_ID+".config.debug";
    public static class Debug
    {
        public static final ConfigBoolean DEBUG_MESSAGES            = new ConfigBoolean("debugMessages",false).apply(DEBUG_KEY);
        public static final ConfigBoolean CONFIG_ELEMENT_DEBUG      = new ConfigBoolean("configElementDebug", false).apply(DEBUG_KEY);
        public static final ConfigBoolean INPUT_CANCELLATION_DEBUG  = new ConfigBoolean("inputCancellationDebugging", false).apply(DEBUG_KEY);
        public static final ConfigBoolean KEYBIND_DEBUG             = new ConfigBoolean("keybindDebugging", false).apply(DEBUG_KEY);
        public static final ConfigBoolean KEYBIND_DEBUG_ACTIONBAR   = new ConfigBoolean("keybindDebuggingIngame", false).apply(DEBUG_KEY);
        public static final ConfigBoolean MOUSE_SCROLL_DEBUG        = new ConfigBoolean("mouseScrollDebug", false).apply(DEBUG_KEY);
        public static final ConfigBoolean PRINT_TRANSLATION_KEYS    = new ConfigBoolean("printTranslationKeys", false).apply(DEBUG_KEY);

        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                DEBUG_MESSAGES,
                CONFIG_ELEMENT_DEBUG,
                INPUT_CANCELLATION_DEBUG,
                KEYBIND_DEBUG,
                KEYBIND_DEBUG_ACTIONBAR,
                MOUSE_SCROLL_DEBUG,
                PRINT_TRANSLATION_KEYS
        );

        public static final List<IHotkey> HOTKEY_LIST = ImmutableList.of(
        );
    }

    private static final String TEST_KEY = MaLiLibReference.MOD_ID+".config.test";
    private static final KeybindSettings OVERLAY_TOGGLE = KeybindSettings.create(KeybindSettings.Context.ANY, KeyAction.PRESS, true, true, false, true);
    //private static final KeybindSettings GUI_RELAXED = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, true, false, false, false);
    private static final KeybindSettings GUI_RELAXED_CANCEL = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, true, false, false, true);
    //private static final KeybindSettings GUI_NO_ORDER = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, false, false, false, true);

    // Stuff used by any Post-Rewrite Code
    private static final String EXPERIMENTAL_KEY = MaLiLibReference.MOD_ID+".config.experimental";
    public static class Experimental
    {
        // Generic
        public static final ConfigBoolean           SORT_CONFIGS_BY_NAME            = new ConfigBoolean("sortConfigsByName", false).apply(EXPERIMENTAL_KEY);
        public static final ConfigBoolean           SORT_EXTENSION_MOD_OPTIONS      = new ConfigBoolean("sortExtensionModOptions", false).apply(EXPERIMENTAL_KEY);

        // Internal
        public static final ConfigString            ACTIVE_CONFIG_PROFILE           = new ConfigString("activeConfigProfile", "").apply(EXPERIMENTAL_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                // Generic
                SORT_CONFIGS_BY_NAME,
                SORT_EXTENSION_MOD_OPTIONS,

                // Internal
                ACTIVE_CONFIG_PROFILE
        );
    }

    public static void loadFromFile()
    {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(CONFIG_FILE_NAME);

        if (Files.exists(configFile) && Files.isReadable(configFile))
        {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "Debug", Debug.OPTIONS);

                if (MaLiLibReference.DEBUG_MODE)
                {
                    ConfigUtils.readConfigBase(root, "Test", Test.OPTIONS);
                    ConfigUtils.readHotkeyToggleOptions(root, "TestEnumHotkeys", "TestEnumToggles", ConfigTestEnum.VALUES);
                }

                if (MaLiLibReference.EXPERIMENTAL_MODE)
                {
                    ConfigUtils.readConfigBase(root, "Experimental", Experimental.OPTIONS);
                }

                //MaLiLib.debugLog("loadFromFile(): Successfully loaded config file '{}'.", configFile.toAbsolutePath());
            }
            else
            {
                MaLiLib.LOGGER.error("loadFromFile(): Failed to parse config file '{}' as a JSON element.", configFile.toAbsolutePath());
            }
        }
        /*
        else
        {
            MaLiLib.LOGGER.error("loadFromFile(): Failed to load config file '{}'.", configFile.toAbsolutePath());
        }
         */
    }

    public static void saveToFile()
    {
        Path dir = FileUtils.getConfigDirectoryAsPath();

        if (!Files.exists(dir))
        {
            FileUtils.createDirectoriesIfMissing(dir);
            //MaLiLib.debugLog("saveToFile(): Creating directory '{}'.", dir.toAbsolutePath());
        }

        if (Files.isDirectory(dir))
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Debug", Debug.OPTIONS);

            if (MaLiLibReference.DEBUG_MODE)
            {
                ConfigUtils.writeConfigBase(root, "Test", Test.OPTIONS);
                ConfigUtils.writeHotkeyToggleOptions(root, "TestEnumHotkeys", "TestEnumToggles", ConfigTestEnum.VALUES);
            }

            if (MaLiLibReference.EXPERIMENTAL_MODE)
            {
                ConfigUtils.writeConfigBase(root, "Experimental", Experimental.OPTIONS);
            }

            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(CONFIG_FILE_NAME));

            /*
            if (JsonUtils.writeJsonToFileAsPath(root, config))
            {
                MaLiLib.debugLog("saveToFile(): Successfully saved config file '{}'.", config.toAbsolutePath());
            }
            else
            {
                MaLiLib.LOGGER.error("saveToFile(): Failed to save config file '{}'.", config.toAbsolutePath());
            }
             */
        }
        /*
        else
        {
            MaLiLib.LOGGER.error("saveToFile(): Config Folder '{}' does not exist!", dir.toAbsolutePath());
        }
         */
    }

    @Override
    public void onConfigsChanged()
    {
        saveToFile();
        loadFromFile();
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
