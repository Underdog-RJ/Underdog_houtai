package com.atguigu.eduservice.entity;



import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.36.2)",
    comments = "Source: image_reco.proto")
public final class ImageRecoGrpc {

  private ImageRecoGrpc() {}

  public static final String SERVICE_NAME = "ImageReco";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ImageRecoOuterClass.ImageDto,
      ImageRecoOuterClass.ResImageDto> getGetAllImageFromAIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAllImageFromAI",
      requestType = ImageRecoOuterClass.ImageDto.class,
      responseType = ImageRecoOuterClass.ResImageDto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ImageRecoOuterClass.ImageDto,
      ImageRecoOuterClass.ResImageDto> getGetAllImageFromAIMethod() {
    io.grpc.MethodDescriptor<ImageRecoOuterClass.ImageDto, ImageRecoOuterClass.ResImageDto> getGetAllImageFromAIMethod;
    if ((getGetAllImageFromAIMethod = ImageRecoGrpc.getGetAllImageFromAIMethod) == null) {
      synchronized (ImageRecoGrpc.class) {
        if ((getGetAllImageFromAIMethod = ImageRecoGrpc.getGetAllImageFromAIMethod) == null) {
          ImageRecoGrpc.getGetAllImageFromAIMethod = getGetAllImageFromAIMethod =
              io.grpc.MethodDescriptor.<ImageRecoOuterClass.ImageDto, ImageRecoOuterClass.ResImageDto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAllImageFromAI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ImageRecoOuterClass.ImageDto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ImageRecoOuterClass.ResImageDto.getDefaultInstance()))
              .setSchemaDescriptor(new ImageRecoMethodDescriptorSupplier("getAllImageFromAI"))
              .build();
        }
      }
    }
    return getGetAllImageFromAIMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ImageRecoStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageRecoStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageRecoStub>() {
        @Override
        public ImageRecoStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageRecoStub(channel, callOptions);
        }
      };
    return ImageRecoStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ImageRecoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageRecoBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageRecoBlockingStub>() {
        @Override
        public ImageRecoBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageRecoBlockingStub(channel, callOptions);
        }
      };
    return ImageRecoBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ImageRecoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageRecoFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageRecoFutureStub>() {
        @Override
        public ImageRecoFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageRecoFutureStub(channel, callOptions);
        }
      };
    return ImageRecoFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ImageRecoImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAllImageFromAI(ImageRecoOuterClass.ImageDto request,
                                  io.grpc.stub.StreamObserver<ImageRecoOuterClass.ResImageDto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllImageFromAIMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAllImageFromAIMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                ImageRecoOuterClass.ImageDto,
                ImageRecoOuterClass.ResImageDto>(
                  this, METHODID_GET_ALL_IMAGE_FROM_AI)))
          .build();
    }
  }

  /**
   */
  public static final class ImageRecoStub extends io.grpc.stub.AbstractAsyncStub<ImageRecoStub> {
    private ImageRecoStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ImageRecoStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageRecoStub(channel, callOptions);
    }

    /**
     */
    public void getAllImageFromAI(ImageRecoOuterClass.ImageDto request,
                                  io.grpc.stub.StreamObserver<ImageRecoOuterClass.ResImageDto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllImageFromAIMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ImageRecoBlockingStub extends io.grpc.stub.AbstractBlockingStub<ImageRecoBlockingStub> {
    private ImageRecoBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ImageRecoBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageRecoBlockingStub(channel, callOptions);
    }

    /**
     */
    public ImageRecoOuterClass.ResImageDto getAllImageFromAI(ImageRecoOuterClass.ImageDto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllImageFromAIMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ImageRecoFutureStub extends io.grpc.stub.AbstractFutureStub<ImageRecoFutureStub> {
    private ImageRecoFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ImageRecoFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageRecoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ImageRecoOuterClass.ResImageDto> getAllImageFromAI(
        ImageRecoOuterClass.ImageDto request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllImageFromAIMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ALL_IMAGE_FROM_AI = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ImageRecoImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ImageRecoImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL_IMAGE_FROM_AI:
          serviceImpl.getAllImageFromAI((ImageRecoOuterClass.ImageDto) request,
              (io.grpc.stub.StreamObserver<ImageRecoOuterClass.ResImageDto>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ImageRecoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ImageRecoBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ImageRecoOuterClass.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ImageReco");
    }
  }

  private static final class ImageRecoFileDescriptorSupplier
      extends ImageRecoBaseDescriptorSupplier {
    ImageRecoFileDescriptorSupplier() {}
  }

  private static final class ImageRecoMethodDescriptorSupplier
      extends ImageRecoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ImageRecoMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ImageRecoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ImageRecoFileDescriptorSupplier())
              .addMethod(getGetAllImageFromAIMethod())
              .build();
        }
      }
    }
    return result;
  }
}
