cmake_minimum_required(VERSION 3.4.1)

add_definitions(-DANDROID )
include_directories(src/main/native/crypto)

find_library( log-lib
              log )

add_library( cryptxts
			 SHARED
             src/main/native/xts/app-xts.c )

target_link_libraries( cryptxts
                       ${log-lib} )

add_library( cryptaes
			 SHARED
             src/main/native/aes/app-aes.c 
             src/main/native/aes/aescrypt.c 
             src/main/native/aes/aeskey.c 
             src/main/native/aes/aestab.c )

target_link_libraries( cryptaes
                       ${log-lib} )

add_library( cryptserpent
			 SHARED
             src/main/native/serpent/app-serpent.c
             src/main/native/serpent/serpent.c )

target_link_libraries( cryptserpent
                       ${log-lib} )

add_library( crypttwofish
			 SHARED
             src/main/native/twofish/app-twofish.c
             src/main/native/twofish/twofish.c )

add_library( cryptripemd160
			 SHARED
             src/main/native/ripemd160/app-ripemd160.c )

target_link_libraries( cryptripemd160
                       ${log-lib} )

add_library( cryptwhirlpool
			 SHARED
             src/main/native/whirlpool/app-whirlpool.c
             src/main/native/whirlpool/whirlpool.c )

target_link_libraries( cryptwhirlpool
                       ${log-lib} )

add_library( fdraio
			 SHARED
             src/main/native/fdraio/fdraio.c )

target_compile_definitions( fdraio
        					PRIVATE _FILE_OFFSET_BITS=64 )

target_link_libraries( fdraio
                       ${log-lib} )

add_library( localxts
			 SHARED
             src/main/native/localxts/localxts.c )

target_compile_definitions( localxts
        					PRIVATE _FILE_OFFSET_BITS=64 )

target_link_libraries( localxts
                       cryptxts
                       ${log-lib} )

add_library( cryptctr
       SHARED
             src/main/native/ctr/app-ctr.c )

target_link_libraries( cryptctr
                       ${log-lib} )

add_library( cryptcbc
       SHARED
             src/main/native/cbc/app-cbc.c )

target_link_libraries( cryptcbc
                       ${log-lib} )

add_library( cryptcfb
       SHARED
             src/main/native/cfb/app-cfb.c )

target_link_libraries( cryptcfb
                       ${log-lib} )

add_library( cryptgost
       SHARED
             src/main/native/gost/app-gost.c
             src/main/native/gost/gost89.c )

target_link_libraries( cryptgost
                       ${log-lib} )

add_library( fsexfat
       SHARED
             src/main/native/util/jniutil.c
             src/main/native/fsexfat/raio.c
             src/main/native/fsexfat/fsexfat.c
             src/main/native/fsexfat/libexfat/cluster.c
             src/main/native/fsexfat/libexfat/io.c
             src/main/native/fsexfat/libexfat/log.c
             src/main/native/fsexfat/libexfat/lookup.c
             src/main/native/fsexfat/libexfat/mount.c
             src/main/native/fsexfat/libexfat/node.c
             src/main/native/fsexfat/libexfat/time.c
             src/main/native/fsexfat/libexfat/utf.c
             src/main/native/fsexfat/libexfat/utils.c
             src/main/native/fsexfat/mkfs/cbm.c
             src/main/native/fsexfat/mkfs/fat.c
             src/main/native/fsexfat/mkfs/main.c
             src/main/native/fsexfat/mkfs/mkexfat.c
             src/main/native/fsexfat/mkfs/rootdir.c
             src/main/native/fsexfat/mkfs/uct.c
             src/main/native/fsexfat/mkfs/uctc.c
             src/main/native/fsexfat/mkfs/vbr.c)

target_compile_definitions( fsexfat
                  PRIVATE _FILE_OFFSET_BITS=64)

target_include_directories( fsexfat
              PRIVATE src/main/native/fsexfat/libexfat/ )

target_link_libraries( fsexfat
                       ${log-lib} )
