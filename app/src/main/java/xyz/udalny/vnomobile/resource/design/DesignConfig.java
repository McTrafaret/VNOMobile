package xyz.udalny.vnomobile.resource.design;

// I thought about making some cool class to contain all settings
// but its just too much and I don't have time to name all the properties
// so if somebody wants, go ahead.
/*
class DesignConfig {

    @IniPropertyName("default")
    public Color defaultColor;

    @IniPropertyName("edit_username")
    public Color inputBoxBackgroundColor;
    @IniPropertyName("edit_username_font_color")
    public Color inputBoxForegroundColor;

    @IniPropertyName("edit_password")
    public Color passwordBoxBackgroundColor;
    @IniPropertyName("edit_password_font_color")
    public Color passwordBoxForegroundColor;

    @IniPropertyName("label7_font_color")
    public Color rememberMeFontColor;
    @IniPropertyName("label_as_font_color")
    public Color asFontColor;
    @IniPropertyName("label_version_font_color")
    public Color versionFontColor;

    @IniPropertyName("panel_btn_identify")
    public Color identifyButtonBackgroundColor;
    @IniPropertyName("panel_btn_identify_font_color")
    public Color identifyButtonForegroundColor;

    @IniPropertyName("panel_btn_createaccount")
    public Color createAccountButtonBackgroundColor;
    @IniPropertyName("panel_btn_createaccount_font_color")
    public Color createAccountButtonForegroundColor;


    @IniPropertyName("listbox_servers")
    public Color serversListBackgroundColor;
    @IniPropertyName("listbox_servers_font_color")
    public Color serversListForegroundColor;

    @IniPropertyName("memo_serverdesc")
    public Color serverDescriptionBackgroundColor;

    @IniPropertyName("memo_serverdesc_font_color")
    public Color serverDescriptionFontColor;

    @IniPropertyName("memo_psa")
    public Color psaBackgroundColor;
    @IniPropertyName("memo_psa_font_color")
    public Color psaFontColor;

    @IniPropertyName("gauge1_back")
    public Color loadingBarBackgroundColor;
    @IniPropertyName("gauge1_fore")
    public Color loadingBarForegroundColor;

    @IniPropertyName("label_load_font_color")
    public Color loadingBarFontColor;

    @IniPropertyName("label_playercount_font_color")
    public Color label_playercount_font_color;

    @IniPropertyName("panel_btn_srvrlst_vip")
    public Color panel_btn_srvrlst_vip;

    @IniPropertyName("panel_btn_srvrlst_vip_font_color")
    public Color panel_btn_srvrlst_vip_font_color;

    @IniPropertyName("panel_btn_srvrlst_vip_on")
    public Color panel_btn_srvrlst_vip_on;

    @IniPropertyName("panel_btn_srvrlst_favorite")
    public Color panel_btn_srvrlst_favorite;

    @IniPropertyName("panel_btn_srvrlst_favorite_font_color")
    public Color panel_btn_srvrlst_favorite_font_color;

    @IniPropertyName("panel_btn_srvrlst_favorite_on")
    public Color panel_btn_srvrlst_favorite_on;

    @IniPropertyName("panel_btn_srvrlst_community")
    public Color panel_btn_srvrlst_community;

    @IniPropertyName("panel_btn_srvrlst_community_font_color")
    public Color panel_btn_srvrlst_community_font_color;

    @IniPropertyName("panel_btn_srvrlst_community_on")
    public Color panel_btn_srvrlst_community_on;

    @IniPropertyName("panel_btn_connect")
    public Color panel_btn_connect;

    @IniPropertyName("panel_btn_connect_font_color")
    public Color panel_btn_connect_font_color;

    @IniPropertyName("panel_btn_connect_on")
    public Color panel_btn_connect_on;

    @IniPropertyName("label_charname_font_color")
    public Color label_charname_font_color;

    @IniPropertyName("panel_random")
    public Color panel_random;

    @IniPropertyName("panel_random_font_color")
    public Color panel_random_font_color;

    @IniPropertyName("panel_select")
    public Color panel_select;

    @IniPropertyName("panel_select_font_color")
    public Color panel_select_font_color;

    @IniPropertyName("memo_ooc")
    public Color memo_ooc;

    @IniPropertyName("memo_ooc_font_color")
    public Color memo_ooc_font_color;

    @IniPropertyName("edit_ooc")
    public Color edit_ooc;

    @IniPropertyName("edit_ooc_font_color")
    public Color edit_ooc_font_color;

    @IniPropertyName("memo_fi")
    public Color memo_fi;

    @IniPropertyName("memo_fi_font_color")
    public Color memo_fi_font_color;

    @IniPropertyName("edit_fi")
    public Color edit_fi;

    @IniPropertyName("edit_fi_font_color")
    public Color edit_fi_font_color;

    @IniPropertyName("memo_debug")
    public Color memo_debug;

    @IniPropertyName("memo_debug_font_color")
    public Color memo_debug_font_color;

    @IniPropertyName("dbtext1_font_color")
    public Color dbtext1_font_color;

    @IniPropertyName("objectcolor memo_chat")
    public String objectcolor memo_chat;

    @IniPropertyName("edit_chat")
    public Color edit_chat;

    @IniPropertyName("panel_btn_ooc")
    public Color panel_btn_ooc;

    @IniPropertyName("panel_btn_ooc_font_color")
    public Color panel_btn_ooc_font_color;

    @IniPropertyName("panel_btn_ooc_on")
    public Color panel_btn_ooc_on;

    @IniPropertyName("panel_btn_debug")
    public Color panel_btn_debug;

    @IniPropertyName("panel_btn_debug_font_color")
    public Color panel_btn_debug_font_color;

    @IniPropertyName("panel_btn_debug_on")
    public Color panel_btn_debug_on;

    @IniPropertyName("panel_btn_event")
    public Color panel_btn_event;

    @IniPropertyName("panel_btn_event_font_color")
    public Color panel_btn_event_font_color;

    @IniPropertyName("panel_btn_event_on")
    public Color panel_btn_event_on;

    @IniPropertyName("hp_left")
    public Color hp_left;

    @IniPropertyName("hp_left_back")
    public Color hp_left_back;

    @IniPropertyName("hp_right")
    public Color hp_right;

    @IniPropertyName("hp_right_back")
    public Color hp_right_back;

    @IniPropertyName("listbox_music")
    public Color listbox_music;

    @IniPropertyName("listbox_area")
    public Color listbox_area;

    @IniPropertyName("listbox_item")
    public Color listbox_item;

    @IniPropertyName("listbox_item_missing")
    public Color listbox_item_missing;

    @IniPropertyName("phone_label_font_color")
    public Color phone_label_font_color;

    @IniPropertyName("phone_listbox")
    public Color phone_listbox;

    @IniPropertyName("phone_listbox_font_color")
    public Color phone_listbox_font_color;

    @IniPropertyName("phone_textbox")
    public Color phone_textbox;

    @IniPropertyName("phone_textbox_font_color")
    public Color phone_textbox_font_color;

    @IniPropertyName("edit_username")
    public String edit_username;

    @IniPropertyName("edit_username_size")
    public String edit_username_size;

    @IniPropertyName("edit_password")
    public String edit_password;

    @IniPropertyName("edit_password_size")
    public String edit_password_size;

    @IniPropertyName("label7")
    public String label7;

    @IniPropertyName("label7_size")
    public String label7_size;

    @IniPropertyName("label_as")
    public String label_as;

    @IniPropertyName("label_as_size")
    public String label_as_size;

    @IniPropertyName("label_version")
    public String label_version;

    @IniPropertyName("label_version_size")
    public String label_version_size;

    @IniPropertyName("panel_btn_identify")
    public String panel_btn_identify;

    @IniPropertyName("panel_btn_identify_size")
    public String panel_btn_identify_size;

    @IniPropertyName("panel_btn_createaccount")
    public String panel_btn_createaccount;

    @IniPropertyName("panel_btn_createaccount_size")
    public String panel_btn_createaccount_size;

    @IniPropertyName("listbox_servers")
    public String listbox_servers;

    @IniPropertyName("listbox_servers_size")
    public String listbox_servers_size;

    @IniPropertyName("memo_serverdesc")
    public String memo_serverdesc;

    @IniPropertyName("memo_serverdesc_size")
    public String memo_serverdesc_size;

    @IniPropertyName("memo_psa")
    public String memo_psa;

    @IniPropertyName("memo_psa_size")
    public String memo_psa_size;

    @IniPropertyName("label_load")
    public String label_load;

    @IniPropertyName("label_load_size")
    public String label_load_size;

    @IniPropertyName("label_playercount")
    public String label_playercount;

    @IniPropertyName("label_playercount_size")
    public String label_playercount_size;

    @IniPropertyName("panel_btn_srvrlst_vip")
    public String panel_btn_srvrlst_vip;

    @IniPropertyName("panel_btn_srvrlst_vip_size")
    public String panel_btn_srvrlst_vip_size;

    @IniPropertyName("panel_btn_srvrlst_favorite")
    public String panel_btn_srvrlst_favorite;

    @IniPropertyName("panel_btn_srvrlst_favorite_size")
    public String panel_btn_srvrlst_favorite_size;

    @IniPropertyName("panel_btn_srvrlst_community")
    public String panel_btn_srvrlst_community;

    @IniPropertyName("panel_btn_srvrlst_community_size")
    public String panel_btn_srvrlst_community_size;

    @IniPropertyName("panel_btn_connect")
    public String panel_btn_connect;

    @IniPropertyName("panel_btn_connect_size")
    public String panel_btn_connect_size;

    @IniPropertyName("label_charname")
    public String label_charname;

    @IniPropertyName("label_charname_size")
    public String label_charname_size;

    @IniPropertyName("panel_random")
    public String panel_random;

    @IniPropertyName("panel_random_size")
    public String panel_random_size;

    @IniPropertyName("panel_select")
    public String panel_select;

    @IniPropertyName("panel_select_size")
    public String panel_select_size;

    @IniPropertyName("edit_chat")
    public String edit_chat;

    @IniPropertyName("edit_chat_size")
    public String edit_chat_size;

    @IniPropertyName("memo_ooc")
    public String memo_ooc;

    @IniPropertyName("memo_ooc_size")
    public String memo_ooc_size;

    @IniPropertyName("edit_ooc")
    public String edit_ooc;

    @IniPropertyName("edit_ooc_size")
    public String edit_ooc_size;

    @IniPropertyName("memo_fi")
    public String memo_fi;

    @IniPropertyName("memo_fi_size")
    public String memo_fi_size;

    @IniPropertyName("edit_fi")
    public String edit_fi;

    @IniPropertyName("edit_size")
    public String edit_size;

    @IniPropertyName("memo_debug")
    public String memo_debug;

    @IniPropertyName("memo_debug_size")
    public String memo_debug_size;

    @IniPropertyName("dbtext1")
    public String dbtext1;

    @IniPropertyName("dbtext1_size")
    public String dbtext1_size;

    @IniPropertyName("memo_textbox")
    public String memo_textbox;

    @IniPropertyName("memo_textbox_size")
    public String memo_textbox_size;

    @IniPropertyName("edit_name")
    public String edit_name;

    @IniPropertyName("edit_name_size")
    public String edit_name_size;

    @IniPropertyName("panel_btn_ooc")
    public String panel_btn_ooc;

    @IniPropertyName("panel_btn_ooc_size")
    public String panel_btn_ooc_size;

    @IniPropertyName("panel_btn_debug")
    public String panel_btn_debug;

    @IniPropertyName("panel_btn_debug_size")
    public String panel_btn_debug_size;

    @IniPropertyName("panel_btn_event")
    public String panel_btn_event;

    @IniPropertyName("panel_btn_event_size")
    public String panel_btn_event_size;

    @IniPropertyName("listbox_music")
    public String listbox_music;

    @IniPropertyName("listbox_music_size")
    public String listbox_music_size;

    @IniPropertyName("listbox_area")
    public String listbox_area;

    @IniPropertyName("listbox_area_size")
    public String listbox_area_size;

    @IniPropertyName("phone_label")
    public String phone_label;

    @IniPropertyName("phone_listbox")
    public String phone_listbox;

    @IniPropertyName("phone_textbox")
    public String phone_textbox;

    @IniPropertyName("edit_name_updiv")
    public String edit_name_updiv;

    @IniPropertyName("edit_name_leftdiv")
    public String edit_name_leftdiv;

    @IniPropertyName("edit_name_align")
    public String edit_name_align;

    @IniPropertyName("memo_textbox_updiv")
    public int memo_textbox_updiv;

    @IniPropertyName("memo_textbox_leftdiv")
    public int memo_textbox_leftdiv;

    @IniPropertyName("badge_updiv")
    public String badge_updiv;

    @IniPropertyName("badge_leftdiv")
    public String badge_leftdiv;

    @IniPropertyName("arrow_updiv")
    public String arrow_updiv;

    @IniPropertyName("arrow_leftdiv")
    public String arrow_leftdiv;


    public int nameTextOffsetY;
    public int nameTextOffsetX;
    public Alignment nameTextAlignment;

    public int chatTextOffsetY;
    public int chatTextOffsetX;

    public int badgeOffsetY;
    public int badgeOffsetX;

    public int arrowOffsetY;
    public int arrowOffsetX;
}
*/