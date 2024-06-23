package com.integration.service.service.impl;

import com.google.protobuf.FloatValue;
import com.google.protobuf.StringValue;
import com.integration.Product;
import com.integration.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.List;

@Service
public class ProductServiceImpl {

    /*@GrpcClient("productServiceChannel")
    private ManagedChannel channel;*/


    public void search() {

            /*ProductServiceGrpc.ProductServiceBlockingStub stub = ProductServiceGrpc.newBlockingStub(channel);

            Product.ProductSearchFilter searchFilter = Product.ProductSearchFilter.newBuilder().setName(StringValue.of("Teriyaki Sauce"))
                    .setPricePerUnit(FloatValue.newBuilder().build()).build();
            Product.ProductsResponse response = stub.search(searchFilter);*/
            //System.out.println("\n\n\n\nresponse====>"+response.toString());

    }
}
