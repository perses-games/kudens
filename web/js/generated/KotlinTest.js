(function (Kotlin) {
  'use strict';
  var _ = Kotlin.defineRootPackage(null, /** @lends _ */ {
    com: Kotlin.definePackage(null, /** @lends _.com */ {
      persesgames: Kotlin.definePackage(function () {
        this.vertexShaderSource = '\n    attribute vec2 a_position;\n    attribute vec3 a_color;\n\n    uniform mat4 u_projectionView;\n\n    varying vec3 v_color;\n\n    void main(void) {\n        v_color = a_color;\n        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);\n    }\n';
        this.fragmentShaderSource = '\n    precision mediump float;\n\n    varying vec3 v_color;\n\n    void main(void) {\n        gl_FragColor = vec4(v_color, 1.0);\n    }\n';
        this.game = null;
        this.start = (new Date()).getTime();
        this.time = (new Date()).getTime();
      }, /** @lends _.com.persesgames */ {
        Test: Kotlin.createClass(null, function (context3d) {
          this.context3d = context3d;
          this.red = 1.0;
          this.green = 1.0;
          this.blue = 0.0;
          this.pMatrix = new _.com.persesgames.math.Matrix4();
          var vainfo = [new _.com.persesgames.shader.VertextAttributeInfo('a_position', 2), new _.com.persesgames.shader.VertextAttributeInfo('a_color', 3)];
          this.program = new _.com.persesgames.shader.ShaderProgram(this.context3d, WebGLRenderingContext.TRIANGLES, _.com.persesgames.vertexShaderSource, _.com.persesgames.fragmentShaderSource, vainfo);
          this.triangle = new Float32Array([0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0]);
        }, /** @lends _.com.persesgames.Test.prototype */ {
          update_14dthe$: function (time) {
            this.red = Math.abs(Math.sin(time * 0.5));
            this.green = Math.abs(Math.cos(time * 0.3));
          },
          render: function () {
            this.context3d.clearColor(this.red, this.green, this.blue, 1.0);
            this.context3d.clear(WebGLRenderingContext.COLOR_BUFFER_BIT);
            this.program.begin();
            this.program.setUniformMatrix4fv_pphpxd$('u_projectionView', this.pMatrix.get());
            this.program.queueVertices_b5uka5$(this.triangle);
            this.program.end();
          }
        }),
        loop$f: function (it) {
          _.com.persesgames.loop();
        },
        loop: function () {
          var testInstance = _.com.persesgames.game;
          if (testInstance != null) {
            _.com.persesgames.time = (new Date()).getTime();
            testInstance.update_14dthe$((_.com.persesgames.time - _.com.persesgames.start) / 1000.0);
            testInstance.render();
          }
          window.requestAnimationFrame(_.com.persesgames.loop$f);
        },
        main_kand9s$: function (args) {
          var tmp$0, tmp$1;
          Kotlin.println('Hello!');
          var webGlElement = (tmp$0 = document.getElementById('canvas')) != null ? tmp$0 : Kotlin.throwNPE();
          var context3d = (tmp$1 = webGlElement.getContext('webgl')) != null ? tmp$1 : Kotlin.throwNPE();
          _.com.persesgames.texture.Textures.load_h0kzx1$(context3d, 'SHIP', 'images/ship2.png');
          _.com.persesgames.game = new _.com.persesgames.Test(context3d);
          _.com.persesgames.loop();
        },
        texture: Kotlin.definePackage(function () {
          this.vertexShaderSource = '\n    attribute vec2 a_position;\n\n    attribute float a_imagesX;\n    attribute float a_imagesY;\n    attribute float a_currentImage;\n\n    uniform mat4 u_projectionView;\n\n    varying float v_imagesX;\n    varying float v_imagesY;\n    varying float v_currentImage;\n\n    void main(void) {\n        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);\n        gl_PointSize = 50.0 / gl_Position.w;\n\n        v_imagesX = a_imagesX;\n        v_imagesY = a_imagesY;\n        v_currentImage = a_currentImage;\n    }\n';
          this.fragmentShaderSource = '\n    precision mediump float;\n\n    uniform sampler2D uSampler;\n\n    varying float v_imagesX;\n    varying float v_imagesY;\n    varying float v_currentImage;\n\n    void main(void) {\n        // calculate current texture coords depending on current image number\n        float blockX = 1.0 / v_imagesX;\n        float blockY = 1.0 / v_imagesY;\n\n        float x = blockX * (mod(v_currentImage, v_imagesX));\n        float y = blockY * floor(v_currentImage / v_imagesY);\n\n        vec2 textCoord = vec2(x + blockX * gl_PointCoord.s, y + blockY - blockY * gl_PointCoord.t);\n        //vec2 textCoord = vec2((x + blockX) * 0.0001 + gl_PointCoord.s, (y + blockY) * 0.0001 + gl_PointCoord.t);\n\n        gl_FragColor = texture2D(uSampler, textCoord);\n    }\n';
          this.Textures = Kotlin.createObject(null, function () {
            this.textures = new Kotlin.DefaultPrimitiveHashMap();
            this.startedLoading = 0;
            this.loaded = 0;
          }, {
            load_h0kzx1$: function (gl, name, filename) {
              this.startedLoading++;
              var webGlTexture = {v: gl.createTexture()};
              if (webGlTexture.v != null) {
                var image = {v: document.createElement('img')};
                image.v.onload = _.com.persesgames.texture.load_h0kzx1$f(gl, webGlTexture, image, this, name);
                image.v.src = filename;
              }
               else {
                Kotlin.println("Couldn't create webgl texture!");
              }
            },
            textureLoaded_ok0n47$: function (gl, texture, image) {
              gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
              gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1);
              gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image);
              gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST);
              gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST);
              gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
            },
            ready: function () {
              return this.loaded === this.startedLoading;
            },
            get_61zpoe$: function (name) {
              var tmp$0;
              tmp$0 = this.textures.get_za3rmp$(name);
              if (tmp$0 == null)
                throw new Kotlin.IllegalArgumentException('Texture with name ' + name + ' is not loaded!');
              return tmp$0;
            },
            clear: function () {
            }
          });
        }, /** @lends _.com.persesgames.texture */ {
          load_h0kzx1$f: function (gl, webGlTexture, image, this$Textures, name) {
            return function (it) {
              this$Textures.textureLoaded_ok0n47$(gl, webGlTexture.v, image.v);
              this$Textures.textures.put_wn2jw4$(name, webGlTexture.v);
              return this$Textures.loaded++;
            };
          }
        }),
        shader: Kotlin.definePackage(null, /** @lends _.com.persesgames.shader */ {
          VertextAttributeInfo: Kotlin.createClass(null, function (locationName, numElements) {
            this.locationName = locationName;
            this.numElements = numElements;
            this.location = 0;
            this.offset = 0;
          }),
          ShaderProgram: Kotlin.createClass(null, function (webgl, mode, vertexShaderSource, fragmentShaderSource, vainfo) {
            var tmp$0, tmp$1, tmp$2, tmp$3, tmp$4;
            this.webgl = webgl;
            this.mode = mode;
            this.vainfo = vainfo;
            this.verticesBlockSize = 0;
            this.currentIndex = 0;
            this.verticesLength = 0;
            this.vertices = new Float32Array(0);
            tmp$0 = this.webgl.createShader(WebGLRenderingContext.VERTEX_SHADER);
            if (tmp$0 == null)
              throw new Kotlin.IllegalStateException('Unable to request vertex shader from webgl context!');
            this.vertex = tmp$0;
            this.webgl.shaderSource(this.vertex, vertexShaderSource);
            this.webgl.compileShader(this.vertex);
            tmp$1 = this.webgl.createShader(WebGLRenderingContext.FRAGMENT_SHADER);
            if (tmp$1 == null)
              throw new Kotlin.IllegalStateException('Unable to request fragment shader from webgl context!');
            this.fragment = tmp$1;
            this.webgl.shaderSource(this.fragment, fragmentShaderSource);
            this.webgl.compileShader(this.fragment);
            tmp$2 = this.webgl.createProgram();
            if (tmp$2 == null)
              throw new Kotlin.IllegalStateException('Unable to request shader program from webgl context!');
            this.shaderProgram = tmp$2;
            this.webgl.attachShader(this.shaderProgram, this.vertex);
            this.webgl.attachShader(this.shaderProgram, this.fragment);
            this.webgl.linkProgram(this.shaderProgram);
            if (Kotlin.equals(this.webgl.getShaderParameter(this.vertex, WebGLRenderingContext.COMPILE_STATUS), false)) {
              Kotlin.println(this.webgl.getShaderInfoLog(this.vertex));
              throw new Kotlin.IllegalStateException('Unable to compile vertex shader!');
            }
            if (Kotlin.equals(this.webgl.getShaderParameter(this.fragment, WebGLRenderingContext.COMPILE_STATUS), false)) {
              Kotlin.println(this.webgl.getShaderInfoLog(this.fragment));
              throw new Kotlin.IllegalStateException('Unable to compile fragment shader!');
            }
            if (Kotlin.equals(this.webgl.getProgramParameter(this.shaderProgram, WebGLRenderingContext.LINK_STATUS), false)) {
              Kotlin.println(this.webgl.getProgramInfoLog(this.shaderProgram));
              throw new Kotlin.IllegalStateException('Unable to compile program!');
            }
            this.webgl.useProgram(this.shaderProgram);
            this.verticesBlockSize = 0;
            tmp$3 = Kotlin.modules['stdlib'].kotlin.collections.iterator_123wqf$(Kotlin.arrayIterator(this.vainfo));
            while (tmp$3.hasNext()) {
              var info = tmp$3.next();
              info.location = this.webgl.getAttribLocation(this.shaderProgram, info.locationName);
              info.offset = this.verticesBlockSize;
              this.verticesBlockSize += info.numElements;
              Kotlin.println('attrib: ' + info.locationName + ', info.location: ' + info.location + ', info.offset: ' + info.offset);
            }
            Kotlin.println('verticesBlockSize ' + this.verticesBlockSize);
            this.currentIndex = 0;
            this.verticesLength = 4096 - 4096 % this.verticesBlockSize;
            this.vertices = new Float32Array(this.verticesLength);
            Kotlin.println('vertices.length ' + this.vertices.length);
            tmp$4 = this.webgl.createBuffer();
            if (tmp$4 == null)
              throw new Kotlin.IllegalStateException('Unable to create webgl buffer!');
            this.attribBuffer = tmp$4;
            this.webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, this.attribBuffer);
            Kotlin.println('ShaderProgram constructor done');
          }, /** @lends _.com.persesgames.shader.ShaderProgram.prototype */ {
            queueVertices_b5uka5$: function (verts) {
              if (this.currentIndex + verts.length >= this.verticesLength) {
                this.flush();
              }
              this.vertices.set(verts, this.currentIndex);
              this.currentIndex += verts.length;
            },
            begin: function () {
              var tmp$0;
              this.webgl.useProgram(this.shaderProgram);
              this.webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, this.attribBuffer);
              this.currentIndex = 0;
              tmp$0 = Kotlin.modules['stdlib'].kotlin.collections.iterator_123wqf$(Kotlin.arrayIterator(this.vainfo));
              while (tmp$0.hasNext()) {
                var info = tmp$0.next();
                this.webgl.enableVertexAttribArray(info.location);
                this.webgl.vertexAttribPointer(info.location, info.numElements, WebGLRenderingContext.FLOAT, false, this.verticesBlockSize * 4, info.offset * 4);
              }
            },
            flush: function () {
              if (this.currentIndex > 0) {
                this.webgl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, this.vertices, WebGLRenderingContext.DYNAMIC_DRAW);
                this.webgl.drawArrays(this.mode, 0, this.currentIndex / this.verticesBlockSize | 0);
                this.currentIndex = 0;
              }
            },
            end: function () {
              this.flush();
              this.webgl.useProgram(null);
            },
            getAttribLocation_61zpoe$: function (location) {
              return this.webgl.getAttribLocation(this.shaderProgram, location);
            },
            getUniformLocation_61zpoe$: function (location) {
              return this.webgl.getUniformLocation(this.shaderProgram, location);
            },
            setUniform1f_9sobi5$: function (location, value) {
              this.flush();
              this.webgl.uniform1f(this.getUniformLocation_61zpoe$(location), value);
            },
            setUniform4f_kjn4ou$: function (location, v1, v2, v3, v4) {
              this.flush();
              this.webgl.uniform4f(this.getUniformLocation_61zpoe$(location), v1, v2, v3, v4);
            },
            setUniform1i_bm4lxs$: function (location, value) {
              this.flush();
              this.webgl.uniform1i(this.getUniformLocation_61zpoe$(location), value);
            },
            setUniformMatrix4fv_pphpxd$: function (location, value) {
              this.flush();
              this.webgl.uniformMatrix4fv(this.getUniformLocation_61zpoe$(location), false, value);
            }
          })
        }),
        math: Kotlin.definePackage(null, /** @lends _.com.persesgames.math */ {
          Matrix4: Kotlin.createClass(null, function () {
            this.matrix = new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$([1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0]));
            this.temp = new Float32Array(16);
            this.translateMatrix_l4igr0$ = new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$([1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0]));
            this.scaleMatrix_vu4fg8$ = new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$([1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0]));
            this.rotateXMatrix_vipfol$ = new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$([1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0]));
            this.rotateYMatrix_gub5gk$ = new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$([1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0]));
            this.rotateZMatrix_25wv8j$ = new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$([1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0]));
          }, /** @lends _.com.persesgames.math.Matrix4.prototype */ {
            get: function () {
              return this.matrix;
            },
            set_b5uka5$: function (values) {
              if (values.length !== 16) {
                throw new Kotlin.IllegalArgumentException('FloatArray must hava 16 entries!');
              }
              this.matrix = values;
            },
            setPerspectiveProjection_7b5o5w$: function (angle, imageAspectRatio, near, far) {
              var r = angle / 180.0 * Math.PI;
              var f = 1.0 / Math.tan(r / 2.0);
              this.matrix.set(0, f / imageAspectRatio);
              this.matrix.set(1, 0.0);
              this.matrix.set(2, 0.0);
              this.matrix.set(3, 0.0);
              this.matrix.set(4, 0.0);
              this.matrix.set(5, f);
              this.matrix.set(6, 0.0);
              this.matrix.set(7, 0.0);
              this.matrix.set(8, 0.0);
              this.matrix.set(9, 0.0);
              this.matrix.set(10, -(far + near) / (far - near));
              this.matrix.set(11, -1.0);
              this.matrix.set(12, 0.0);
              this.matrix.set(13, 0.0);
              this.matrix.set(14, -(2.0 * far * near) / (far - near));
              this.matrix.set(15, 0.0);
            },
            setToIdentity: function () {
              this.matrix.set(0, 1.0);
              this.matrix.set(1, 0.0);
              this.matrix.set(2, 0.0);
              this.matrix.set(3, 0.0);
              this.matrix.set(4, 0.0);
              this.matrix.set(5, 1.0);
              this.matrix.set(6, 0.0);
              this.matrix.set(7, 0.0);
              this.matrix.set(8, 0.0);
              this.matrix.set(9, 0.0);
              this.matrix.set(10, 1.0);
              this.matrix.set(11, 0.0);
              this.matrix.set(12, 0.0);
              this.matrix.set(13, 0.0);
              this.matrix.set(14, 0.0);
              this.matrix.set(15, 1.0);
            },
            mul_jx4e45$: function (other) {
              this.mul(other.get());
            },
            mul: function (other) {
              if (other.length !== 16) {
                throw new Kotlin.IllegalArgumentException('FloatArray must hava 16 entries!');
              }
              this.temp.set(0, this.matrix.get(0) * other.get(0) + this.matrix.get(1) * other.get(4) + this.matrix.get(2) * other.get(8) + this.matrix.get(3) * other.get(12));
              this.temp.set(1, this.matrix.get(0) * other.get(1) + this.matrix.get(1) * other.get(5) + this.matrix.get(2) * other.get(9) + this.matrix.get(3) * other.get(13));
              this.temp.set(2, this.matrix.get(0) * other.get(2) + this.matrix.get(1) * other.get(6) + this.matrix.get(2) * other.get(10) + this.matrix.get(3) * other.get(14));
              this.temp.set(3, this.matrix.get(0) * other.get(3) + this.matrix.get(1) * other.get(7) + this.matrix.get(2) * other.get(11) + this.matrix.get(3) * other.get(15));
              this.temp.set(4, this.matrix.get(4) * other.get(0) + this.matrix.get(5) * other.get(4) + this.matrix.get(6) * other.get(8) + this.matrix.get(7) * other.get(12));
              this.temp.set(5, this.matrix.get(4) * other.get(1) + this.matrix.get(5) * other.get(5) + this.matrix.get(6) * other.get(9) + this.matrix.get(7) * other.get(13));
              this.temp.set(6, this.matrix.get(4) * other.get(2) + this.matrix.get(5) * other.get(6) + this.matrix.get(6) * other.get(10) + this.matrix.get(7) * other.get(14));
              this.temp.set(7, this.matrix.get(4) * other.get(3) + this.matrix.get(5) * other.get(7) + this.matrix.get(6) * other.get(11) + this.matrix.get(7) * other.get(15));
              this.temp.set(8, this.matrix.get(8) * other.get(0) + this.matrix.get(9) * other.get(4) + this.matrix.get(10) * other.get(8) + this.matrix.get(11) * other.get(12));
              this.temp.set(9, this.matrix.get(8) * other.get(1) + this.matrix.get(9) * other.get(5) + this.matrix.get(10) * other.get(9) + this.matrix.get(11) * other.get(13));
              this.temp.set(10, this.matrix.get(8) * other.get(2) + this.matrix.get(9) * other.get(6) + this.matrix.get(10) * other.get(10) + this.matrix.get(11) * other.get(14));
              this.temp.set(11, this.matrix.get(8) * other.get(3) + this.matrix.get(9) * other.get(7) + this.matrix.get(10) * other.get(11) + this.matrix.get(11) * other.get(15));
              this.temp.set(12, this.matrix.get(12) * other.get(0) + this.matrix.get(13) * other.get(4) + this.matrix.get(14) * other.get(8) + this.matrix.get(15) * other.get(12));
              this.temp.set(13, this.matrix.get(12) * other.get(1) + this.matrix.get(13) * other.get(5) + this.matrix.get(14) * other.get(9) + this.matrix.get(15) * other.get(13));
              this.temp.set(14, this.matrix.get(12) * other.get(2) + this.matrix.get(13) * other.get(6) + this.matrix.get(14) * other.get(10) + this.matrix.get(15) * other.get(14));
              this.temp.set(15, this.matrix.get(12) * other.get(3) + this.matrix.get(13) * other.get(7) + this.matrix.get(14) * other.get(11) + this.matrix.get(15) * other.get(15));
              this.matrix.set(0, this.temp.get(0));
              this.matrix.set(1, this.temp.get(1));
              this.matrix.set(2, this.temp.get(2));
              this.matrix.set(3, this.temp.get(3));
              this.matrix.set(4, this.temp.get(4));
              this.matrix.set(5, this.temp.get(5));
              this.matrix.set(6, this.temp.get(6));
              this.matrix.set(7, this.temp.get(7));
              this.matrix.set(8, this.temp.get(8));
              this.matrix.set(9, this.temp.get(9));
              this.matrix.set(10, this.temp.get(10));
              this.matrix.set(11, this.temp.get(11));
              this.matrix.set(12, this.temp.get(12));
              this.matrix.set(13, this.temp.get(13));
              this.matrix.set(14, this.temp.get(14));
              this.matrix.set(15, this.temp.get(15));
            },
            translate_y2kzbl$: function (x, y, z) {
              this.translateMatrix_l4igr0$.set(12, x);
              this.translateMatrix_l4igr0$.set(13, y);
              this.translateMatrix_l4igr0$.set(14, z);
              this.mul(this.translateMatrix_l4igr0$);
            },
            scale_y2kzbl$: function (x, y, z) {
              this.scaleMatrix_vu4fg8$.set(0, x);
              this.scaleMatrix_vu4fg8$.set(5, y);
              this.scaleMatrix_vu4fg8$.set(10, z);
              this.mul(this.scaleMatrix_vu4fg8$);
            },
            rotateX_mx4ult$: function (angle) {
              this.rotateXMatrix_vipfol$.set(5, Math.cos(angle));
              this.rotateXMatrix_vipfol$.set(6, -Math.sin(angle));
              this.rotateXMatrix_vipfol$.set(9, Math.sin(angle));
              this.rotateXMatrix_vipfol$.set(10, Math.cos(angle));
              this.mul(this.rotateXMatrix_vipfol$);
            },
            rotateY_mx4ult$: function (angle) {
              this.rotateYMatrix_gub5gk$.set(0, Math.cos(angle));
              this.rotateYMatrix_gub5gk$.set(2, Math.sin(angle));
              this.rotateYMatrix_gub5gk$.set(8, -Math.sin(angle));
              this.rotateYMatrix_gub5gk$.set(10, Math.cos(angle));
              this.mul(this.rotateYMatrix_gub5gk$);
            },
            rotateZ_mx4ult$: function (angle) {
              this.rotateZMatrix_25wv8j$.set(0, Math.cos(angle));
              this.rotateZMatrix_25wv8j$.set(1, Math.sin(angle));
              this.rotateZMatrix_25wv8j$.set(4, -Math.sin(angle));
              this.rotateZMatrix_25wv8j$.set(5, Math.cos(angle));
              this.mul(this.rotateZMatrix_25wv8j$);
            }
          })
        })
      })
    })
  });
  Kotlin.defineModule('KotlinTest', _);
  _.com.persesgames.main_kand9s$([]);
}(Kotlin));

//@ sourceMappingURL=KotlinTest.js.map
