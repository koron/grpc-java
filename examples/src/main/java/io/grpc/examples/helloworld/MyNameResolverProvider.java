package io.grpc.examples.helloworld;

import java.net.URI;
import java.util.ArrayList;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import io.grpc.internal.DnsNameResolverProvider;

public class MyNameResolverProvider extends NameResolverProvider {
    private static final MyNameResolverProvider INSTANCE = new MyNameResolverProvider();

    public static MyNameResolverProvider getInstance() {
        return INSTANCE;
    }

    private DnsNameResolverProvider base;
    private ArrayList<NameResolver> resolvers = new ArrayList<>();

    public MyNameResolverProvider() {
        base = new DnsNameResolverProvider();
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        NameResolver r = base.newNameResolver(targetUri, params);
        if (r != null) {
            resolvers.add(r);
        }
        return r;
    }

    @Override
    public String getDefaultScheme() {
        return base.getDefaultScheme();
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }

    public void refresh() {
        for (NameResolver r : resolvers) {
            r.refresh();
        }
    }
}
