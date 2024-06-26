package wo1261931780.stdataAuthorityControl.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

/**
 * Created by Intellij IDEA.
 * Project:st-dataAuthorityControl
 * Package:wo1261931780.stdataAuthorityControl.config
 *
 * @author liujiajun_junw
 * @Date 2024-05-22-23  星期五
 * @Description
 */
@AllArgsConstructor
@Getter
public enum DataPermission {


	// 枚举类型根据范围从前往后排列，避免影响getScope
	// Scope 数据权限范围 ：ALL（全部）、DEPT（部门）、MYSELF（自己）
	DATA_MANAGER("数据管理员", "DATA_MANAGER", DataScope.ALL),
	DATA_AUDITOR("数据审核员", "DATA_AUDITOR", DataScope.DEPT),
	DATA_OPERATOR("数据业务员", "DATA_OPERATOR", DataScope.MYSELF);

	private String name;
	private String code;
	private DataScope scope;


	public static String getName(String code) {
		for (DataPermission type : DataPermission.values()) {
			if (type.getCode().equals(code)) {
				return type.getName();
			}
		}
		return null;
	}

	public static String getCode(String name) {
		for (DataPermission type : DataPermission.values()) {
			if (type.getName().equals(name)) {
				return type.getCode();
			}
		}
		return null;
	}

	public static DataScope getScope(Collection<String> code) {
		for (DataPermission type : DataPermission.values()) {
			for (String v : code) {
				if (type.getCode().equals(v)) {
					return type.getScope();
				}
			}
		}
		return DataScope.MYSELF;
	}
}
