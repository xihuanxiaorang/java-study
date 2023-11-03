package fun.xiaorang.oss.model.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/23 13:26
 */
@Schema(description = "分页结果")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PageResult<T> {
    /**
     * 总记录数
     */
    @Schema(description = "总记录数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long counts;
    /**
     * 每页记录数
     */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageSize;
    /**
     * 当前页码
     */
    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long page;
    /**
     * 分页数据
     */
    @Schema(description = "分页数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> items;

    public static <T> PageResult<T> of(List<T> items, Long counts, Long pageSize, Long page) {
        return PageResult.<T>builder()
                .items(items)
                .counts(counts)
                .pageSize(pageSize)
                .page(page)
                .build();
    }
}
