package com.xnx3.wangmarket.plugin.upgrade;

import com.xnx3.CacheUtil;
import com.xnx3.cache.JavaUtil;
import com.xnx3.j2ee.pluginManage.interfaces.DatabaseLoadFinishInterface;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.kefu.core.Global;

/**
 * 更新版本更新的相关数据
 * @author 管雷鸣
 *
 */
public class Update implements DatabaseLoadFinishInterface{

	@Override
	public void databaseLoadFinish() {
		SystemUtil.put("UPGRADE_PLUGIN_SHELL_FOLDER", "https://gitee.com/leimingyun/yunkefu_deploy/raw/master/upgrade/");
		SystemUtil.put("UPGRADE_PLUGIN_VERSION", Global.VERSION);
		
		ConsoleUtil.debug("set UPGRADE_PLUGIN_VERSION : "+SystemUtil.get("UPGRADE_PLUGIN_VERSION"));
	}
	
}
