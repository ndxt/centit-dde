package com.centit.dde.filter;

import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 请求级数据库连接兜底清理 Filter。
 * 业务代码通过 {@link AbstractSourceConnectThreadHolder#fetchConnect} 获取的数据库连接
 * 缓存在当前线程的 ThreadLocal 中，正常应由事务切面(@MetadataJdbcTransaction)或
 * BizOptFlow.run 在事务边界统一释放(commitAndRelease/rollbackAndRelease)。
 * 一旦某个调用路径漏调释放方法，连接会以 active 状态一直挂在 ThreadLocal 上，
 * HikariCP 既收不回(非 idle)也无法靠 maxLifetime 回收(归还时才检查)，
 * 最终导致连接池耗尽(Connection is not available, request timed out)。
 * 本 Filter 在每个请求结束时强制清理当前线程的 ThreadLocal 连接
 * (回滚未提交事务 + 归还连接 + 清除 ThreadLocal)，作为最后一道安全网，
 * 保证 Tomcat 线程被复用时不会携带上一个请求遗留的连接。
 * 当前线程没有持有连接时清理为空操作，开销极小。
 */
@Component
public class SourceConnectCleanupFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            // 无论请求正常完成还是抛异常，都强制归还当前线程占用的连接
            AbstractSourceConnectThreadHolder.cleanupCurrentThread();
        }
    }

    @Override
    public void destroy() {
    }
}
