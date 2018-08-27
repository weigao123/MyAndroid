package com.garfield.lint_plugin.util
/**
 * Created by jiangyiwang on 2018/4/8.
 */
class CmdExecutor {

    static String execute(String cmd, File dir) {
        Process p = cmd.execute(null, dir)
        String result = p.text.trim()
        return result
    }
}
