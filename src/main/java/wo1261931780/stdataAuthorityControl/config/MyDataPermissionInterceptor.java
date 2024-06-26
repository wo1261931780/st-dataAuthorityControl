package wo1261931780.stdataAuthorityControl.config;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.*;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Intellij IDEA.
 * Project:st-dataAuthorityControl
 * Package:wo1261931780.stdataAuthorityControl.config
 *
 * @author liujiajun_junw
 * @Date 2024-05-22-16  星期五
 * @Description 拦截器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MyDataPermissionInterceptor extends JsqlParserSupport implements InnerInterceptor {

	/**
	 * 数据权限处理器
	 */
	private MyDataPermissionHandler dataPermissionHandler;

	// @Override
	public void beforeQuery(Executor executor,
	                        MappedStatement ms,
	                        Object parameter,
	                        RowBounds rowBounds,
	                        ResultHandler resultHandler,
	                        BoundSql boundSql) throws SQLException {
		if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
			return;
		}
		PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
		mpBs.sql(this.parserSingle(mpBs.sql(), ms.getId()));
	}

	@Override
	protected void processSelect(Select select, int index, String sql, Object obj) {
		SelectBody selectBody = select.getSelectBody();
		if (selectBody instanceof PlainSelect) {
			this.setWhere((PlainSelect) selectBody, (String) obj);
		} else if (selectBody instanceof SetOperationList) {
			SetOperationList setOperationList = (SetOperationList) selectBody;
			List<SelectBody> selectBodyList = setOperationList.getSelects();
			selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, (String) obj));
		}
	}

	/**
	 * 设置 where 条件
	 *
	 * @param plainSelect  查询对象
	 * @param whereSegment 查询条件片段
	 */
	private void setWhere(PlainSelect plainSelect, String whereSegment) {

		Expression sqlSegment = this.dataPermissionHandler.getSqlSegment(plainSelect, whereSegment);
		if (null != sqlSegment) {
			plainSelect.setWhere(sqlSegment);
		}
	}
}
