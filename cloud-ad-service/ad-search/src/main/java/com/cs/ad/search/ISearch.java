package com.cs.ad.search;

import com.cs.ad.search.vo.SearchRequest;
import com.cs.ad.search.vo.SearchResponse;

/**
 * @author fucker
 * 请求/响应的服务接口
 * 媒体发起请求，传递进来请求对象
 * 检索服务做出响应，返回对应的响应对象
 */
public interface ISearch {
    /**传入search请求对象，返回search响应对象*/
    SearchResponse fetchAds(SearchRequest searchRequest);
}
