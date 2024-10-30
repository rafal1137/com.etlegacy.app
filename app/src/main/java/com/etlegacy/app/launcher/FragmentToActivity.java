package com.etlegacy.app.launcher;

public interface FragmentToActivity {
    //m_data default
    void default_path(String comm);
    //m_data choosen directory
    void new_path(String comm);
    //m_command additonal temp comamnds
    void additional_commands(String comm);

    void fs_game(String comm);
}
