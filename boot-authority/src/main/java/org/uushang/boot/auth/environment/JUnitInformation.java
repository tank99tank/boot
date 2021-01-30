package org.uushang.boot.auth.environment;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

import org.uushang.boot.auth.context.parser.Information;

public class JUnitInformation implements Information {
	private String owinfo_4a_info = "{\"userId\":\"junitUserId\",\"userNo\":\"1234\",\"cardNumber\":\"1234\",\"orgNo\":\"1234\",\"orgName\":\"\",\"orgCreditCode\":\"1234\",\"username\":\"junitUserName\",\"password\":\"junitPassword\",\"nickname\":\"junitNickName\",\"email\":\"junitEmail\",\"telephone\":\"junitTelephone\",\"address\":\"junitAddress\",\"fullPath\":\"\\\\junit\\\\junit\\\\junit\"}";

	private String customs_h4a_info = "{\"userId\":\"junitUserId\",\"userName\":\"junitUserName\",\"orgId\":\"1234\",\"orgName\":\"\",\"allPathName\":\"\"}";

	private String info;

	public JUnitInformation(Project project, String info) {
		if (Objects.nonNull(info)) {
			this.info = info;
		} else {
			if (Project.OWINFO_4A == project)
				this.info = this.owinfo_4a_info;
			if (Project.CUSTOMS_H4A == project)
				this.info = this.customs_h4a_info;
		}
	}

	public Object getInformation(Object source) throws Exception {
		return JSONObject.parseObject(this.info);
	}

	public JUnitInformation() {
	}
}
