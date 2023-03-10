package com.atguigu.eduservice.entity;// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: image_reco.proto

public final class ImageRecoOuterClass {
  private ImageRecoOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ImageDtoOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ImageDto)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string image_path = 1;</code>
     */
    String getImagePath();
    /**
     * <code>string image_path = 1;</code>
     */
    com.google.protobuf.ByteString
        getImagePathBytes();
  }
  /**
   * Protobuf type {@code ImageDto}
   */
  public  static final class ImageDto extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ImageDto)
      ImageDtoOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use ImageDto.newBuilder() to construct.
    private ImageDto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ImageDto() {
      imagePath_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new ImageDto();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ImageDto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              String s = input.readStringRequireUtf8();

              imagePath_ = s;
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ImageRecoOuterClass.internal_static_ImageDto_descriptor;
    }

    @Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ImageRecoOuterClass.internal_static_ImageDto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ImageDto.class, Builder.class);
    }

    public static final int IMAGE_PATH_FIELD_NUMBER = 1;
    private volatile Object imagePath_;
    /**
     * <code>string image_path = 1;</code>
     */
    public String getImagePath() {
      Object ref = imagePath_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        imagePath_ = s;
        return s;
      }
    }
    /**
     * <code>string image_path = 1;</code>
     */
    public com.google.protobuf.ByteString
        getImagePathBytes() {
      Object ref = imagePath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        imagePath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getImagePathBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, imagePath_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getImagePathBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, imagePath_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ImageDto)) {
        return super.equals(obj);
      }
      ImageDto other = (ImageDto) obj;

      if (!getImagePath()
          .equals(other.getImagePath())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + IMAGE_PATH_FIELD_NUMBER;
      hash = (53 * hash) + getImagePath().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ImageDto parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ImageDto parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ImageDto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ImageDto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ImageDto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ImageDto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ImageDto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ImageDto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ImageDto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ImageDto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ImageDto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ImageDto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ImageDto prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ImageDto}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ImageDto)
        ImageDtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ImageRecoOuterClass.internal_static_ImageDto_descriptor;
      }

      @Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ImageRecoOuterClass.internal_static_ImageDto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ImageDto.class, Builder.class);
      }

      // Construct using ImageRecoOuterClass.ImageDto.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        imagePath_ = "";

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ImageRecoOuterClass.internal_static_ImageDto_descriptor;
      }

      @Override
      public ImageDto getDefaultInstanceForType() {
        return ImageDto.getDefaultInstance();
      }

      @Override
      public ImageDto build() {
        ImageDto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public ImageDto buildPartial() {
        ImageDto result = new ImageDto(this);
        result.imagePath_ = imagePath_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ImageDto) {
          return mergeFrom((ImageDto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ImageDto other) {
        if (other == ImageDto.getDefaultInstance()) return this;
        if (!other.getImagePath().isEmpty()) {
          imagePath_ = other.imagePath_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ImageDto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ImageDto) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object imagePath_ = "";
      /**
       * <code>string image_path = 1;</code>
       */
      public String getImagePath() {
        Object ref = imagePath_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          imagePath_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string image_path = 1;</code>
       */
      public com.google.protobuf.ByteString
          getImagePathBytes() {
        Object ref = imagePath_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          imagePath_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string image_path = 1;</code>
       */
      public Builder setImagePath(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        imagePath_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string image_path = 1;</code>
       */
      public Builder clearImagePath() {
        
        imagePath_ = getDefaultInstance().getImagePath();
        onChanged();
        return this;
      }
      /**
       * <code>string image_path = 1;</code>
       */
      public Builder setImagePathBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        imagePath_ = value;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:ImageDto)
    }

    // @@protoc_insertion_point(class_scope:ImageDto)
    private static final ImageDto DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ImageDto();
    }

    public static ImageDto getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ImageDto>
        PARSER = new com.google.protobuf.AbstractParser<ImageDto>() {
      @Override
      public ImageDto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ImageDto(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ImageDto> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<ImageDto> getParserForType() {
      return PARSER;
    }

    @Override
    public ImageDto getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public interface ResImageDtoOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ResImageDto)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>repeated string list_path = 1;</code>
     */
    java.util.List<String>
        getListPathList();
    /**
     * <code>repeated string list_path = 1;</code>
     */
    int getListPathCount();
    /**
     * <code>repeated string list_path = 1;</code>
     */
    String getListPath(int index);
    /**
     * <code>repeated string list_path = 1;</code>
     */
    com.google.protobuf.ByteString
        getListPathBytes(int index);
  }
  /**
   * Protobuf type {@code ResImageDto}
   */
  public  static final class ResImageDto extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ResImageDto)
      ResImageDtoOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use ResImageDto.newBuilder() to construct.
    private ResImageDto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ResImageDto() {
      listPath_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new ResImageDto();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ResImageDto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                listPath_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000001;
              }
              listPath_.add(s);
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) != 0)) {
          listPath_ = listPath_.getUnmodifiableView();
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ImageRecoOuterClass.internal_static_ResImageDto_descriptor;
    }

    @Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ImageRecoOuterClass.internal_static_ResImageDto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ResImageDto.class, Builder.class);
    }

    public static final int LIST_PATH_FIELD_NUMBER = 1;
    private com.google.protobuf.LazyStringList listPath_;
    /**
     * <code>repeated string list_path = 1;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getListPathList() {
      return listPath_;
    }
    /**
     * <code>repeated string list_path = 1;</code>
     */
    public int getListPathCount() {
      return listPath_.size();
    }
    /**
     * <code>repeated string list_path = 1;</code>
     */
    public String getListPath(int index) {
      return listPath_.get(index);
    }
    /**
     * <code>repeated string list_path = 1;</code>
     */
    public com.google.protobuf.ByteString
        getListPathBytes(int index) {
      return listPath_.getByteString(index);
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      for (int i = 0; i < listPath_.size(); i++) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, listPath_.getRaw(i));
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < listPath_.size(); i++) {
          dataSize += computeStringSizeNoTag(listPath_.getRaw(i));
        }
        size += dataSize;
        size += 1 * getListPathList().size();
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ResImageDto)) {
        return super.equals(obj);
      }
      ResImageDto other = (ResImageDto) obj;

      if (!getListPathList()
          .equals(other.getListPathList())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (getListPathCount() > 0) {
        hash = (37 * hash) + LIST_PATH_FIELD_NUMBER;
        hash = (53 * hash) + getListPathList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ResImageDto parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ResImageDto parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ResImageDto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ResImageDto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ResImageDto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ResImageDto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ResImageDto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ResImageDto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ResImageDto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ResImageDto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ResImageDto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ResImageDto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ResImageDto prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ResImageDto}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ResImageDto)
        ResImageDtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ImageRecoOuterClass.internal_static_ResImageDto_descriptor;
      }

      @Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ImageRecoOuterClass.internal_static_ResImageDto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ResImageDto.class, Builder.class);
      }

      // Construct using ImageRecoOuterClass.ResImageDto.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        listPath_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ImageRecoOuterClass.internal_static_ResImageDto_descriptor;
      }

      @Override
      public ResImageDto getDefaultInstanceForType() {
        return ResImageDto.getDefaultInstance();
      }

      @Override
      public ResImageDto build() {
        ResImageDto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public ResImageDto buildPartial() {
        ResImageDto result = new ResImageDto(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) != 0)) {
          listPath_ = listPath_.getUnmodifiableView();
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.listPath_ = listPath_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ResImageDto) {
          return mergeFrom((ResImageDto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ResImageDto other) {
        if (other == ResImageDto.getDefaultInstance()) return this;
        if (!other.listPath_.isEmpty()) {
          if (listPath_.isEmpty()) {
            listPath_ = other.listPath_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureListPathIsMutable();
            listPath_.addAll(other.listPath_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ResImageDto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ResImageDto) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.LazyStringList listPath_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureListPathIsMutable() {
        if (!((bitField0_ & 0x00000001) != 0)) {
          listPath_ = new com.google.protobuf.LazyStringArrayList(listPath_);
          bitField0_ |= 0x00000001;
         }
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public com.google.protobuf.ProtocolStringList
          getListPathList() {
        return listPath_.getUnmodifiableView();
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public int getListPathCount() {
        return listPath_.size();
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public String getListPath(int index) {
        return listPath_.get(index);
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public com.google.protobuf.ByteString
          getListPathBytes(int index) {
        return listPath_.getByteString(index);
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public Builder setListPath(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureListPathIsMutable();
        listPath_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public Builder addListPath(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureListPathIsMutable();
        listPath_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public Builder addAllListPath(
          Iterable<String> values) {
        ensureListPathIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, listPath_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public Builder clearListPath() {
        listPath_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string list_path = 1;</code>
       */
      public Builder addListPathBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        ensureListPathIsMutable();
        listPath_.add(value);
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:ResImageDto)
    }

    // @@protoc_insertion_point(class_scope:ResImageDto)
    private static final ResImageDto DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ResImageDto();
    }

    public static ResImageDto getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ResImageDto>
        PARSER = new com.google.protobuf.AbstractParser<ResImageDto>() {
      @Override
      public ResImageDto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ResImageDto(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ResImageDto> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<ResImageDto> getParserForType() {
      return PARSER;
    }

    @Override
    public ResImageDto getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ImageDto_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ImageDto_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ResImageDto_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ResImageDto_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\020image_reco.proto\"\036\n\010ImageDto\022\022\n\nimage_" +
      "path\030\001 \001(\t\" \n\013ResImageDto\022\021\n\tlist_path\030\001" +
      " \003(\t2;\n\tImageReco\022.\n\021getAllImageFromAI\022\t" +
      ".ImageDto\032\014.ResImageDto\"\000b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_ImageDto_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ImageDto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ImageDto_descriptor,
        new String[] { "ImagePath", });
    internal_static_ResImageDto_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_ResImageDto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ResImageDto_descriptor,
        new String[] { "ListPath", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
