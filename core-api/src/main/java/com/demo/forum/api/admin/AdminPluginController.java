package com.demo.forum.api.admin;

import java.util.Comparator;
import java.util.List;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/plugins")
public class AdminPluginController {
    private final PluginManager pluginManager;

    public AdminPluginController(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @GetMapping
    public PageResponse<PluginSummary> listPlugins(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        List<PluginSummary> all = pluginManager.getPlugins()
                .stream()
                .map(wrapper -> new PluginSummary(
                        wrapper.getPluginId(),
                        wrapper.getDescriptor().getVersion(),
                        wrapper.getDescriptor().getProvider(),
                        toState(wrapper.getPluginState())))
                .sorted(Comparator.comparing(PluginSummary::id))
                .toList();
        int fromIndex = Math.min((safePage - 1) * safeSize, all.size());
        int toIndex = Math.min(fromIndex + safeSize, all.size());
        List<PluginSummary> pageItems = all.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil(all.size() / (double) safeSize);
        return new PageResponse<>(pageItems, safePage, safeSize, all.size(), totalPages);
    }

    private String toState(PluginState state) {
        if (state == null) {
            return "UNKNOWN";
        }
        return state.name();
    }
}
