����   2 � kotlin/UByteArray  cLjava/lang/Object;Ljava/util/Collection<Lkotlin/UByte;>;Lkotlin/jvm/internal/markers/KMappedMarker; java/lang/Object  java/util/Collection  )kotlin/jvm/internal/markers/KMappedMarker  Lkotlin/SinceKotlin; version 1.3 "Lkotlin/ExperimentalUnsignedTypes; getSize ()I size  
   storage [B  	   getSize-impl ([B)I  
   iterator $()Lkotlin/collections/UByteIterator; ()Ljava/util/Iterator;  
   java/util/Iterator   #Lorg/jetbrains/annotations/NotNull; iterator-impl &([B)Lkotlin/collections/UByteIterator; # $
  % contains-7apg3OU (B)Z contains (Ljava/lang/Object;)Z kotlin/UByte + 
unbox-impl ()B - .
 , / ' (
  1 ([BB)Z ' 3
  4 containsAll (Ljava/util/Collection;)Z .(Ljava/util/Collection<+Ljava/lang/Object;>;)Z containsAll-impl ([BLjava/util/Collection;)Z 9 :
  ; isEmpty ()Z isEmpty-impl ([B)Z ? @
  A storage$annotations ()V Lkotlin/PublishedApi; <init> ([B)V  kotlin/jvm/internal/Intrinsics I checkParameterIsNotNull '(Ljava/lang/Object;Ljava/lang/String;)V K L
 J M F D
  O this Lkotlin/UByteArray; get-impl ([BI)B constructor-impl (B)B U V
 , W $this index I set-VurrAj0 ([BIB)V value B kotlin/UByteArray$Iterator ` F G
 a b  kotlin/collections/UByteIterator d kotlin/collections/ArraysKt f ) 3
 g h element +([BLjava/util/Collection<Lkotlin/UByte;>;)Z elements l java/lang/Iterable n = >  p   o r hasNext t > ! u next ()Ljava/lang/Object; w x ! y it Ljava/lang/Object; "$i$a$-all-UByteArray$containsAll$1 
element$iv $this$all$iv Ljava/lang/Iterable; $i$f$all Ljava/util/Collection;  ([B)[B (I)[B U �
  � box-impl ([B)Lkotlin/UByteArray; v �
  b toString-impl ([B)Ljava/lang/String; java/lang/StringBuilder �
 � O UByteArray(storage= � append -(Ljava/lang/String;)Ljava/lang/StringBuilder; � �
 � � java/util/Arrays � toString � �
 � � ) � ()Ljava/lang/String; � �
 � � hashCode-impl hashCode � 
 � � equals-impl ([BLjava/lang/Object;)Z $Lorg/jetbrains/annotations/Nullable; ()[B - �
  � areEqual '(Ljava/lang/Object;Ljava/lang/Object;)Z � �
 J � equals-impl0 ([B[B)Z p1 � p2 � � �
  � � 
  � equals � �
  � add-7apg3OU 'java/lang/UnsupportedOperationException � 3Operation is not supported for read-only collection � (Ljava/lang/String;)V F �
 � � addAll *(Ljava/util/Collection<+Lkotlin/UByte;>;)Z clear remove 	removeAll 	retainAll add toArray ()[Ljava/lang/Object; %kotlin/jvm/internal/CollectionToArray � +(Ljava/util/Collection;)[Ljava/lang/Object; � �
 � � (([Ljava/lang/Object;)[Ljava/lang/Object;  <T:Ljava/lang/Object;>([TT;)[TT; >(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object; � �
 � � Lkotlin/Metadata; mv       bv        k d1���F



��





	
��
	





@��200:-B0ø��¢B0ø��¢	J020Hø��¢J 0200Hø��¢J020HÖJ020Hø��¢J	0HÖJ0H¢ !J"0#H¢$%J#&0'202(0Hø��¢)*J	+0,HÖR08VX¢
R08��X¢
��ø��
¨. d2   Lkotlin/UByte; other get "Lkotlin/collections/UByteIterator; set Iterator kotlin-stdlib UByteArray.kt RuntimeInvisibleAnnotations Code LineNumberTable StackMapTable 	Signature $RuntimeInvisibleParameterAnnotations 
Deprecated LocalVariableTable InnerClasses 
SourceFile RuntimeVisibleAnnotations 1     	      �     "   %     �         *� � �    �        Q    �        *� �    �            �         *� � &�    �        �     "  A    �         *� � !�    �         ' (  �   !     	*� � 5�    �        Q ) *  �   :     +� ,� � �*+� ,� 0� 2�    �    
 �         6 7  �   !     	*� +� <�    �        �    8 �     "    = >  �         *� � B�    �       	 C D  �          �     �     �     E   F G  �   D     +H� N*� P*+� �    �       �        Q R         �     E   �     "    S T  �   C     *3=>� X�    �   
     
  �        Y       Z [   \ ]  �   X     *>6:66T�    �   
    #  $ �         Y       Z [     ^ _  	    �   -     *��    �       ' �        Y    	 # $  �   6     � aY*� c� e�    �       * �        Y    �     "   	 ' 3  �   O      *=:>6� i�    �       5  7  7 �        Y       j _  	 9 :  �  #     s+m� N+� oM>,� � q � � X,� s :� v � E� z ::6� ,� %*� ,� 06:	6
6	� i� � ���� �    �   0 �  o�  !� C  @� �    �  o  �      ;  T  U ? ; _ ; q V �   H  < . { |  ? + } [  8 9 ~ |   g  �   e � [    s Y      s l �  �    k �   	    "   	 ? @  �   @     *�� � �    �    	@ �       > �        Y    	 U �  �   2     *H� N*�    �       �            �   
  E   "   �     "   	 U �  �   1     �� ��    �        �     