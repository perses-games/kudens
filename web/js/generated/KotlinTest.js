(function (Kotlin) {
  'use strict';
  var _ = Kotlin.defineRootPackage(null, /** @lends _ */ {
    com: Kotlin.definePackage(null, /** @lends _.com */ {
      persesgames: Kotlin.definePackage(function () {
        this.vertexShaderSource = '\n    attribute vec2 a_position;\n    attribute vec3 a_color;\n\n    uniform mat4 u_projectionView;\n\n    varying vec3 v_color;\n    varying vec2 v_textCoord;\n\n    void main(void) {\n        v_color = a_color;\n        v_textCoord = a_position.xy;\n\n        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);\n    }\n';
        this.fragmentShaderSource = '\n    precision mediump float;\n\n    uniform sampler2D u_sampler;\n\n    varying vec3 v_color;\n    varying vec2 v_textCoord;\n\n    void main(void) {\n        gl_FragColor = texture2D(u_sampler, v_textCoord) * vec4(v_color, 1.0);\n    }\n';
        this.game = null;
        this.start = (new Date()).getTime();
        this.time = (new Date()).getTime();
      }, /** @lends _.com.persesgames */ {
        throwError_61zpoe$: function (msg) {
          Kotlin.println('ERROR: ' + msg);
          window.alert(msg);
          throw new Kotlin.IllegalStateException(msg);
        },
        Test: Kotlin.createClass(null, function (webgl) {
          this.webgl = webgl;
          this.red = 1.0;
          this.green = 1.0;
          this.blue = 0.0;
          this.rotX = 0.0;
          this.rotY = 0.0;
          this.rotZ = 0.0;
          this.z = -1.0;
          this.mMatrix = new _.com.persesgames.math.Matrix4();
          this.vMatrix = new _.com.persesgames.math.Matrix4();
          this.pMatrix = new _.com.persesgames.math.Matrix4();
          var vainfo = [new _.com.persesgames.shader.VertextAttributeInfo('a_position', 2), new _.com.persesgames.shader.VertextAttributeInfo('a_color', 3)];
          this.program = new _.com.persesgames.shader.ShaderProgram(this.webgl, WebGLRenderingContext.TRIANGLES, _.com.persesgames.vertexShaderSource, _.com.persesgames.fragmentShaderSource, vainfo);
          this.triangle = new Float32Array([0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0]);
          _.com.persesgames.texture.Textures.load_h0kzx1$(this.webgl, 'SHIP', 'images/ship2.png');
        }, /** @lends _.com.persesgames.Test.prototype */ {
          update_14dthe$: function (time) {
            if (!_.com.persesgames.texture.Textures.ready()) {
              return;
            }
            this.red = Math.abs(Math.sin(time * 0.5));
            this.green = Math.abs(Math.cos(time * 0.3));
            this.blue = Math.abs(Math.cos(time * 0.7));
            this.rotX = time / 5.0;
            this.rotY = time / 3.0;
            this.z = -20.0 + Math.sin(time) * 19.0;
          },
          render: function () {
            this.resize();
            if (!_.com.persesgames.texture.Textures.ready()) {
              return;
            }
            this.webgl.clearColor(this.red, this.green, this.blue, 1.0);
            this.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT);
            this.mMatrix.setToIdentity();
            this.mMatrix.translate_y2kzbl$(-0.5, -0.5, 0.0);
            this.mMatrix.scale_y2kzbl$(2.0, 2.0, 1.0);
            this.mMatrix.rotateX_mx4ult$(this.rotX);
            this.mMatrix.rotateY_mx4ult$(this.rotY);
            this.mMatrix.rotateZ_mx4ult$(this.rotX + this.rotY);
            this.mMatrix.translate_y2kzbl$(0.0, 0.0, this.z);
            this.pMatrix.setPerspectiveProjection_7b5o5w$(60.0, window.innerWidth / window.innerHeight, 0.10000000149011612, 100.0);
            this.mMatrix.mul_jx4e45$(this.vMatrix);
            this.mMatrix.mul_jx4e45$(this.pMatrix);
            this.program.begin();
            this.webgl.activeTexture(WebGLRenderingContext.TEXTURE0);
            this.webgl.bindTexture(WebGLRenderingContext.TEXTURE_2D, _.com.persesgames.texture.Textures.get_61zpoe$('SHIP'));
            this.program.setUniform1i_bm4lxs$('u_sampler', 0);
            this.program.setUniformMatrix4fv_pphpxd$('u_projectionView', this.mMatrix.getFloat32Array());
            this.program.queueVertices_b5uka5$(this.triangle);
            this.program.end();
          },
          resize: function () {
            var canvas = this.webgl.canvas;
            if (canvas.width !== (window.innerWidth | 0) || canvas.height !== (window.innerHeight | 0)) {
              canvas.width = window.innerWidth | 0;
              canvas.height = window.innerHeight | 0;
              this.webgl.viewport(0, 0, canvas.width, canvas.height);
            }
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
        main_kand9s$f: function (canvas, webgl) {
          return function (it) {
            canvas.v.width = window.innerWidth | 0;
            canvas.v.height = window.innerHeight | 0;
            webgl.v.viewport(0, 0, canvas.v.width, canvas.v.height);
          };
        },
        main_kand9s$: function (args) {
          var tmp$0, tmp$1;
          Kotlin.println('Hello!');
          var canvas = {v: document.createElement('canvas')};
          ((tmp$0 = document.body) != null ? tmp$0 : Kotlin.throwNPE()).appendChild(canvas.v);
          var webgl = {v: (tmp$1 = canvas.v.getContext('webgl')) != null ? tmp$1 : Kotlin.throwNPE()};
          Kotlin.modules['stdlib'].kotlin.dom.on_9k7t35$(canvas.v, 'resize', true, _.com.persesgames.main_kand9s$f(canvas, webgl));
          _.com.persesgames.texture.Textures.load_h0kzx1$(webgl.v, 'SHIP', 'images/ship2.png');
          _.com.persesgames.game = new _.com.persesgames.Test(webgl.v);
          _.com.persesgames.loop();
        },
        shader: Kotlin.definePackage(null, /** @lends _.com.persesgames.shader */ {
          VertextAttributeInfo: Kotlin.createClass(null, function (locationName, numElements) {
            this.locationName = locationName;
            this.numElements = numElements;
            this.location = 0;
            this.offset = 0;
          }),
          ShaderProgram: Kotlin.createClass(null, function (webgl, mode, vertexShaderSource, fragmentShaderSource, vainfo) {
            var tmp$0, tmp$1, tmp$2;
            this.webgl = webgl;
            this.mode = mode;
            this.vainfo = vainfo;
            this.verticesBlockSize = 0;
            this.currentIndex = 0;
            this.verticesLength = 0;
            this.vertices = new Float32Array(0);
            this.vertex = this.compileShader(vertexShaderSource, WebGLRenderingContext.VERTEX_SHADER);
            this.fragment = this.compileShader(fragmentShaderSource, WebGLRenderingContext.FRAGMENT_SHADER);
            tmp$0 = this.webgl.createProgram();
            if (tmp$0 == null)
              throw new Kotlin.IllegalStateException('Unable to request shader program from webgl context!');
            this.shaderProgram = tmp$0;
            this.webgl.attachShader(this.shaderProgram, this.vertex);
            this.webgl.attachShader(this.shaderProgram, this.fragment);
            this.webgl.linkProgram(this.shaderProgram);
            if (Kotlin.equals(this.webgl.getProgramParameter(this.shaderProgram, WebGLRenderingContext.LINK_STATUS), false)) {
              Kotlin.println(this.webgl.getProgramInfoLog(this.shaderProgram));
              throw new Kotlin.IllegalStateException('Unable to compile program!');
            }
            this.webgl.useProgram(this.shaderProgram);
            this.verticesBlockSize = 0;
            tmp$1 = Kotlin.modules['stdlib'].kotlin.collections.iterator_123wqf$(Kotlin.arrayIterator(this.vainfo));
            while (tmp$1.hasNext()) {
              var info = tmp$1.next();
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
            tmp$2 = this.webgl.createBuffer();
            if (tmp$2 == null)
              throw new Kotlin.IllegalStateException('Unable to create webgl buffer!');
            this.attribBuffer = tmp$2;
            this.webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, this.attribBuffer);
            Kotlin.println('ShaderProgram constructor done');
          }, /** @lends _.com.persesgames.shader.ShaderProgram.prototype */ {
            compileShader: function (source, type) {
              var tmp$0;
              var result;
              tmp$0 = this.webgl.createShader(type);
              if (tmp$0 == null)
                throw new Kotlin.IllegalStateException('Unable to request shader from webgl context!');
              result = tmp$0;
              this.webgl.shaderSource(result, source);
              this.webgl.compileShader(result);
              if (Kotlin.equals(this.webgl.getShaderParameter(result, WebGLRenderingContext.COMPILE_STATUS), false)) {
                throw new Kotlin.IllegalStateException('Unable to compile shader!' + '\n' + source + '\n' + '\n' + Kotlin.toString(this.webgl.getShaderInfoLog(result)));
              }
              return result;
            },
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
            this.matrix = [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0];
            this.temp = Kotlin.numberArrayOfSize(16);
            this.translateMatrix_l4igr0$ = [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0];
            this.scaleMatrix_vu4fg8$ = [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0];
            this.rotateXMatrix_vipfol$ = [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0];
            this.rotateYMatrix_gub5gk$ = [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0];
            this.rotateZMatrix_25wv8j$ = [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0];
          }, /** @lends _.com.persesgames.math.Matrix4.prototype */ {
            get: function () {
              return this.matrix;
            },
            getFloat32Array: function () {
              return new Float32Array(Kotlin.modules['stdlib'].kotlin.collections.toTypedArray_rjqrz0$(this.get()));
            },
            set_q3cr5i$: function (values) {
              if (values.length !== 16) {
                throw new Kotlin.IllegalArgumentException('Matrix size should be 16!');
              }
              this.matrix = values;
            },
            setPerspectiveProjection_7b5o5w$: function (angle, imageAspectRatio, near, far) {
              var r = angle / 180.0 * Math.PI;
              var f = 1.0 / Math.tan(r / 2.0);
              this.matrix[0] = f / imageAspectRatio;
              this.matrix[1] = 0.0;
              this.matrix[2] = 0.0;
              this.matrix[3] = 0.0;
              this.matrix[4] = 0.0;
              this.matrix[5] = f;
              this.matrix[6] = 0.0;
              this.matrix[7] = 0.0;
              this.matrix[8] = 0.0;
              this.matrix[9] = 0.0;
              this.matrix[10] = -(far + near) / (far - near);
              this.matrix[11] = -1.0;
              this.matrix[12] = 0.0;
              this.matrix[13] = 0.0;
              this.matrix[14] = -(2.0 * far * near) / (far - near);
              this.matrix[15] = 0.0;
            },
            setToIdentity: function () {
              this.matrix[0] = 1.0;
              this.matrix[1] = 0.0;
              this.matrix[2] = 0.0;
              this.matrix[3] = 0.0;
              this.matrix[4] = 0.0;
              this.matrix[5] = 1.0;
              this.matrix[6] = 0.0;
              this.matrix[7] = 0.0;
              this.matrix[8] = 0.0;
              this.matrix[9] = 0.0;
              this.matrix[10] = 1.0;
              this.matrix[11] = 0.0;
              this.matrix[12] = 0.0;
              this.matrix[13] = 0.0;
              this.matrix[14] = 0.0;
              this.matrix[15] = 1.0;
            },
            mul_jx4e45$: function (other) {
              this.mul(other.get());
            },
            mul: function (other) {
              if (other.length !== 16) {
                throw new Kotlin.IllegalArgumentException('Matrix size should be 16!');
              }
              this.temp[0] = this.matrix[0] * other[0] + this.matrix[1] * other[4] + this.matrix[2] * other[8] + this.matrix[3] * other[12];
              this.temp[1] = this.matrix[0] * other[1] + this.matrix[1] * other[5] + this.matrix[2] * other[9] + this.matrix[3] * other[13];
              this.temp[2] = this.matrix[0] * other[2] + this.matrix[1] * other[6] + this.matrix[2] * other[10] + this.matrix[3] * other[14];
              this.temp[3] = this.matrix[0] * other[3] + this.matrix[1] * other[7] + this.matrix[2] * other[11] + this.matrix[3] * other[15];
              this.temp[4] = this.matrix[4] * other[0] + this.matrix[5] * other[4] + this.matrix[6] * other[8] + this.matrix[7] * other[12];
              this.temp[5] = this.matrix[4] * other[1] + this.matrix[5] * other[5] + this.matrix[6] * other[9] + this.matrix[7] * other[13];
              this.temp[6] = this.matrix[4] * other[2] + this.matrix[5] * other[6] + this.matrix[6] * other[10] + this.matrix[7] * other[14];
              this.temp[7] = this.matrix[4] * other[3] + this.matrix[5] * other[7] + this.matrix[6] * other[11] + this.matrix[7] * other[15];
              this.temp[8] = this.matrix[8] * other[0] + this.matrix[9] * other[4] + this.matrix[10] * other[8] + this.matrix[11] * other[12];
              this.temp[9] = this.matrix[8] * other[1] + this.matrix[9] * other[5] + this.matrix[10] * other[9] + this.matrix[11] * other[13];
              this.temp[10] = this.matrix[8] * other[2] + this.matrix[9] * other[6] + this.matrix[10] * other[10] + this.matrix[11] * other[14];
              this.temp[11] = this.matrix[8] * other[3] + this.matrix[9] * other[7] + this.matrix[10] * other[11] + this.matrix[11] * other[15];
              this.temp[12] = this.matrix[12] * other[0] + this.matrix[13] * other[4] + this.matrix[14] * other[8] + this.matrix[15] * other[12];
              this.temp[13] = this.matrix[12] * other[1] + this.matrix[13] * other[5] + this.matrix[14] * other[9] + this.matrix[15] * other[13];
              this.temp[14] = this.matrix[12] * other[2] + this.matrix[13] * other[6] + this.matrix[14] * other[10] + this.matrix[15] * other[14];
              this.temp[15] = this.matrix[12] * other[3] + this.matrix[13] * other[7] + this.matrix[14] * other[11] + this.matrix[15] * other[15];
              this.matrix[0] = this.temp[0];
              this.matrix[1] = this.temp[1];
              this.matrix[2] = this.temp[2];
              this.matrix[3] = this.temp[3];
              this.matrix[4] = this.temp[4];
              this.matrix[5] = this.temp[5];
              this.matrix[6] = this.temp[6];
              this.matrix[7] = this.temp[7];
              this.matrix[8] = this.temp[8];
              this.matrix[9] = this.temp[9];
              this.matrix[10] = this.temp[10];
              this.matrix[11] = this.temp[11];
              this.matrix[12] = this.temp[12];
              this.matrix[13] = this.temp[13];
              this.matrix[14] = this.temp[14];
              this.matrix[15] = this.temp[15];
            },
            translate_y2kzbl$: function (x, y, z) {
              this.translateMatrix_l4igr0$[12] = x;
              this.translateMatrix_l4igr0$[13] = y;
              this.translateMatrix_l4igr0$[14] = z;
              this.mul(this.translateMatrix_l4igr0$);
            },
            scale_y2kzbl$: function (x, y, z) {
              this.scaleMatrix_vu4fg8$[0] = x;
              this.scaleMatrix_vu4fg8$[5] = y;
              this.scaleMatrix_vu4fg8$[10] = z;
              this.mul(this.scaleMatrix_vu4fg8$);
            },
            rotateX_mx4ult$: function (angle) {
              this.rotateXMatrix_vipfol$[5] = Math.cos(angle);
              this.rotateXMatrix_vipfol$[6] = -Math.sin(angle);
              this.rotateXMatrix_vipfol$[9] = Math.sin(angle);
              this.rotateXMatrix_vipfol$[10] = Math.cos(angle);
              this.mul(this.rotateXMatrix_vipfol$);
            },
            rotateY_mx4ult$: function (angle) {
              this.rotateYMatrix_gub5gk$[0] = Math.cos(angle);
              this.rotateYMatrix_gub5gk$[2] = Math.sin(angle);
              this.rotateYMatrix_gub5gk$[8] = -Math.sin(angle);
              this.rotateYMatrix_gub5gk$[10] = Math.cos(angle);
              this.mul(this.rotateYMatrix_gub5gk$);
            },
            rotateZ_mx4ult$: function (angle) {
              this.rotateZMatrix_25wv8j$[0] = Math.cos(angle);
              this.rotateZMatrix_25wv8j$[1] = Math.sin(angle);
              this.rotateZMatrix_25wv8j$[4] = -Math.sin(angle);
              this.rotateZMatrix_25wv8j$[5] = Math.cos(angle);
              this.mul(this.rotateZMatrix_25wv8j$);
            }
          })
        }),
        texture: Kotlin.definePackage(function () {
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
          this.vertexShaderSource = '\n    attribute vec2 a_position;\n\n    attribute float a_imagesX;\n    attribute float a_imagesY;\n    attribute float a_currentImage;\n\n    uniform mat4 u_projectionView;\n\n    varying float v_imagesX;\n    varying float v_imagesY;\n    varying float v_currentImage;\n\n    void main(void) {\n        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);\n        gl_PointSize = 50.0 / gl_Position.w;\n\n        v_imagesX = a_imagesX;\n        v_imagesY = a_imagesY;\n        v_currentImage = a_currentImage;\n    }\n';
          this.fragmentShaderSource = '\n    precision mediump float;\n\n    uniform sampler2D uSampler;\n\n    varying float v_imagesX;\n    varying float v_imagesY;\n    varying float v_currentImage;\n\n    void main(void) {\n        // calculate current texture coords depending on current image number\n        float blockX = 1.0 / v_imagesX;\n        float blockY = 1.0 / v_imagesY;\n\n        float x = blockX * (mod(v_currentImage, v_imagesX));\n        float y = blockY * floor(v_currentImage / v_imagesY);\n\n        vec2 textCoord = vec2(x + blockX * gl_PointCoord.s, y + blockY - blockY * gl_PointCoord.t);\n        //vec2 textCoord = vec2((x + blockX) * 0.0001 + gl_PointCoord.s, (y + blockY) * 0.0001 + gl_PointCoord.t);\n\n        gl_FragColor = texture2D(uSampler, textCoord);\n    }\n';
        }, /** @lends _.com.persesgames.texture */ {
          load_h0kzx1$f: function (gl, webGlTexture, image, this$Textures, name) {
            return function (it) {
              this$Textures.textureLoaded_ok0n47$(gl, webGlTexture.v, image.v);
              this$Textures.textures.put_wn2jw4$(name, webGlTexture.v);
              return this$Textures.loaded++;
            };
          }
        })
      })
    })
  });
  Kotlin.defineModule('KotlinTest', _);
  _.com.persesgames.main_kand9s$([]);
}(Kotlin));

//@ sourceMappingURL=KotlinTest.js.map
