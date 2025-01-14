PGDMP     8    7                {            samir    15.2 (Debian 15.2-1.pgdg110+1)    15.2 A    `           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            a           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            b           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            c           1262    16384    samir    DATABASE     p   CREATE DATABASE samir WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE samir;
                samir    false            �            1259    16563 
   beneficios    TABLE     �   CREATE TABLE public.beneficios (
    codigo integer NOT NULL,
    dif boolean NOT NULL,
    inacumulavel character varying(255)[],
    name character varying(255),
    salario13 boolean NOT NULL
);
    DROP TABLE public.beneficios;
       public         heap    samir    false            �            1259    16556    beneficios_seq    SEQUENCE     x   CREATE SEQUENCE public.beneficios_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.beneficios_seq;
       public          samir    false            �            1259    16489    calculo_em_lote    TABLE     �
  CREATE TABLE public.calculo_em_lote (
    id integer NOT NULL,
    acordo real NOT NULL,
    alcada boolean NOT NULL,
    alcada_correcao real NOT NULL,
    alcada_correcao_porcetagem real NOT NULL,
    alcada_juros real NOT NULL,
    alcada_juros_porcentagem real NOT NULL,
    alcada_total real NOT NULL,
    alcada_valor real NOT NULL,
    aps character varying(255),
    atualizacao character varying(255),
    beneficio character varying(255),
    citacao character varying(255),
    competencia_ano_anterior real NOT NULL,
    competencia_ano_atual real NOT NULL,
    correcao bytea,
    cpf character varying(255),
    data bytea,
    data_de_ajuizamento character varying(255),
    data_de_inicio_beneficio_acumulado bytea,
    data_de_pagamento character varying(255),
    data_final_beneficio_acumulado bytea,
    decricao_juros character varying(255),
    descricao_correcao character varying(255),
    devido bytea,
    dib character varying(255),
    dib_anterior character varying(255),
    honorario_advocativos_data character varying(255),
    honorarios_advocativos real NOT NULL,
    i_pvalor_ano_anterior real NOT NULL,
    i_pvalor_ano_atual real NOT NULL,
    inicio_juros character varying(255),
    juros bytea,
    limite_minimo_maximo boolean NOT NULL,
    limite_minimo_maximo_beneficio_acumulado bytea,
    nome character varying(255),
    nome_beneficio_beneficio_acumulado bytea,
    numero_do_beneficio character varying(255),
    numero_do_processo character varying(255),
    pacelas_vencidas real NOT NULL,
    porcentagemrmi real NOT NULL,
    porcentagem_rmi_beneficio_acumulado bytea,
    possui_decimo_terceiro boolean NOT NULL,
    possui_juros boolean NOT NULL,
    reajuste_acumulado bytea,
    reajuste_recebido bytea,
    recebeu_beneficio boolean NOT NULL,
    recebido bytea,
    rmi real NOT NULL,
    rmil_beneficio_acumulado bytea,
    salario bytea,
    salario13beneficio_acumulado bytea,
    salario13_obrigatorio boolean,
    salario13obrigatorio_beneficio_acumulado bytea,
    salario_corrigido bytea,
    salario_juros bytea,
    salario_minimo boolean NOT NULL,
    salario_minimo_beneficio_acumulado bytea,
    salario_total bytea,
    salariominimos_alcada real NOT NULL,
    termo_final character varying(255),
    termo_inicial character varying(255),
    texto_honorarios character varying(255),
    texto_periodo_alcada character varying(255),
    tipo character varying(255),
    tipo_correcao integer NOT NULL,
    tipo_juros integer NOT NULL,
    total_processos real NOT NULL,
    url character varying(255),
    usuario integer NOT NULL,
    valor_honorarios real NOT NULL,
    valor_corrigido real NOT NULL,
    valor_juros real NOT NULL,
    valor_total real NOT NULL
);
 #   DROP TABLE public.calculo_em_lote;
       public         heap    samir    false            �            1259    16496    correcao    TABLE     �   CREATE TABLE public.correcao (
    id integer NOT NULL,
    data date NOT NULL,
    percentual double precision NOT NULL,
    describe_correcao_type integer
);
    DROP TABLE public.correcao;
       public         heap    samir    false            �            1259    16557    correcao_seq    SEQUENCE     v   CREATE SEQUENCE public.correcao_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.correcao_seq;
       public          samir    false            �            1259    16407    describe_correcao    TABLE     �   CREATE TABLE public.describe_correcao (
    id integer NOT NULL,
    describe character varying(255),
    type integer NOT NULL
);
 %   DROP TABLE public.describe_correcao;
       public         heap    samir    false            �            1259    16501    describe_correcao_correcao    TABLE     �   CREATE TABLE public.describe_correcao_correcao (
    describe_correcao_id integer NOT NULL,
    correcao_id integer NOT NULL
);
 .   DROP TABLE public.describe_correcao_correcao;
       public         heap    samir    false            �            1259    16558    describe_correcao_seq    SEQUENCE        CREATE SEQUENCE public.describe_correcao_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.describe_correcao_seq;
       public          samir    false            �            1259    16415    describe_juros    TABLE     �   CREATE TABLE public.describe_juros (
    id integer NOT NULL,
    describe character varying(255),
    type integer NOT NULL
);
 "   DROP TABLE public.describe_juros;
       public         heap    samir    false            �            1259    16504    describe_juros_juros    TABLE     t   CREATE TABLE public.describe_juros_juros (
    describe_juros_id integer NOT NULL,
    juros_id integer NOT NULL
);
 (   DROP TABLE public.describe_juros_juros;
       public         heap    samir    false            �            1259    16559    describe_juros_seq    SEQUENCE     |   CREATE SEQUENCE public.describe_juros_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.describe_juros_seq;
       public          samir    false            �            1259    16458    hibernate_sequence    SEQUENCE     {   CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.hibernate_sequence;
       public          samir    false            �            1259    16507    juros    TABLE     �   CREATE TABLE public.juros (
    id integer NOT NULL,
    data date NOT NULL,
    juros double precision NOT NULL,
    describe_juros_id integer
);
    DROP TABLE public.juros;
       public         heap    samir    false            �            1259    16560 	   juros_seq    SEQUENCE     s   CREATE SEQUENCE public.juros_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.juros_seq;
       public          samir    false            �            1259    16512    salario_minimo    TABLE     h   CREATE TABLE public.salario_minimo (
    id integer NOT NULL,
    data date,
    valor real NOT NULL
);
 "   DROP TABLE public.salario_minimo;
       public         heap    samir    false            �            1259    16561    salario_minimo_seq    SEQUENCE     |   CREATE SEQUENCE public.salario_minimo_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.salario_minimo_seq;
       public          samir    false            �            1259    16517    taxa_reajuste    TABLE     �   CREATE TABLE public.taxa_reajuste (
    codigo integer NOT NULL,
    data date,
    reajuste_acumulado double precision NOT NULL
);
 !   DROP TABLE public.taxa_reajuste;
       public         heap    samir    false            �            1259    16562    taxa_reajuste_seq    SEQUENCE     {   CREATE SEQUENCE public.taxa_reajuste_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.taxa_reajuste_seq;
       public          samir    false            �            1259    16439    usuario    TABLE     z   CREATE TABLE public.usuario (
    id integer NOT NULL,
    cpf character varying(255),
    nome character varying(255)
);
    DROP TABLE public.usuario;
       public         heap    samir    false            �            1259    16438    usuario_id_seq    SEQUENCE     �   ALTER TABLE public.usuario ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          samir    false    217            ]          0    16563 
   beneficios 
   TABLE DATA           P   COPY public.beneficios (codigo, dif, inacumulavel, name, salario13) FROM stdin;
    public          samir    false    233   (Y       O          0    16489    calculo_em_lote 
   TABLE DATA           A  COPY public.calculo_em_lote (id, acordo, alcada, alcada_correcao, alcada_correcao_porcetagem, alcada_juros, alcada_juros_porcentagem, alcada_total, alcada_valor, aps, atualizacao, beneficio, citacao, competencia_ano_anterior, competencia_ano_atual, correcao, cpf, data, data_de_ajuizamento, data_de_inicio_beneficio_acumulado, data_de_pagamento, data_final_beneficio_acumulado, decricao_juros, descricao_correcao, devido, dib, dib_anterior, honorario_advocativos_data, honorarios_advocativos, i_pvalor_ano_anterior, i_pvalor_ano_atual, inicio_juros, juros, limite_minimo_maximo, limite_minimo_maximo_beneficio_acumulado, nome, nome_beneficio_beneficio_acumulado, numero_do_beneficio, numero_do_processo, pacelas_vencidas, porcentagemrmi, porcentagem_rmi_beneficio_acumulado, possui_decimo_terceiro, possui_juros, reajuste_acumulado, reajuste_recebido, recebeu_beneficio, recebido, rmi, rmil_beneficio_acumulado, salario, salario13beneficio_acumulado, salario13_obrigatorio, salario13obrigatorio_beneficio_acumulado, salario_corrigido, salario_juros, salario_minimo, salario_minimo_beneficio_acumulado, salario_total, salariominimos_alcada, termo_final, termo_inicial, texto_honorarios, texto_periodo_alcada, tipo, tipo_correcao, tipo_juros, total_processos, url, usuario, valor_honorarios, valor_corrigido, valor_juros, valor_total) FROM stdin;
    public          samir    false    219   �[       P          0    16496    correcao 
   TABLE DATA           P   COPY public.correcao (id, data, percentual, describe_correcao_type) FROM stdin;
    public          samir    false    220   \       J          0    16407    describe_correcao 
   TABLE DATA           ?   COPY public.describe_correcao (id, describe, type) FROM stdin;
    public          samir    false    214   ol       Q          0    16501    describe_correcao_correcao 
   TABLE DATA           W   COPY public.describe_correcao_correcao (describe_correcao_id, correcao_id) FROM stdin;
    public          samir    false    221   >m       K          0    16415    describe_juros 
   TABLE DATA           <   COPY public.describe_juros (id, describe, type) FROM stdin;
    public          samir    false    215   [m       R          0    16504    describe_juros_juros 
   TABLE DATA           K   COPY public.describe_juros_juros (describe_juros_id, juros_id) FROM stdin;
    public          samir    false    222   .n       S          0    16507    juros 
   TABLE DATA           C   COPY public.juros (id, data, juros, describe_juros_id) FROM stdin;
    public          samir    false    223   Kn       T          0    16512    salario_minimo 
   TABLE DATA           9   COPY public.salario_minimo (id, data, valor) FROM stdin;
    public          samir    false    224   9u       U          0    16517    taxa_reajuste 
   TABLE DATA           I   COPY public.taxa_reajuste (codigo, data, reajuste_acumulado) FROM stdin;
    public          samir    false    225   6v       M          0    16439    usuario 
   TABLE DATA           0   COPY public.usuario (id, cpf, nome) FROM stdin;
    public          samir    false    217          d           0    0    beneficios_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.beneficios_seq', 151, true);
          public          samir    false    226            e           0    0    correcao_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.correcao_seq', 551, true);
          public          samir    false    227            f           0    0    describe_correcao_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.describe_correcao_seq', 1, false);
          public          samir    false    228            g           0    0    describe_juros_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.describe_juros_seq', 1, true);
          public          samir    false    229            h           0    0    hibernate_sequence    SEQUENCE SET     C   SELECT pg_catalog.setval('public.hibernate_sequence', 2269, true);
          public          samir    false    218            i           0    0 	   juros_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.juros_seq', 151, true);
          public          samir    false    230            j           0    0    salario_minimo_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.salario_minimo_seq', 1, false);
          public          samir    false    231            k           0    0    taxa_reajuste_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.taxa_reajuste_seq', 1, false);
          public          samir    false    232            l           0    0    usuario_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.usuario_id_seq', 1, false);
          public          samir    false    216            �           2606    16569    beneficios beneficios_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.beneficios
    ADD CONSTRAINT beneficios_pkey PRIMARY KEY (codigo);
 D   ALTER TABLE ONLY public.beneficios DROP CONSTRAINT beneficios_pkey;
       public            samir    false    233            �           2606    16495 $   calculo_em_lote calculo_em_lote_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.calculo_em_lote
    ADD CONSTRAINT calculo_em_lote_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.calculo_em_lote DROP CONSTRAINT calculo_em_lote_pkey;
       public            samir    false    219            �           2606    16500    correcao correcao_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.correcao
    ADD CONSTRAINT correcao_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.correcao DROP CONSTRAINT correcao_pkey;
       public            samir    false    220            �           2606    16411 (   describe_correcao describe_correcao_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.describe_correcao
    ADD CONSTRAINT describe_correcao_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.describe_correcao DROP CONSTRAINT describe_correcao_pkey;
       public            samir    false    214            �           2606    16419 "   describe_juros describe_juros_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.describe_juros
    ADD CONSTRAINT describe_juros_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.describe_juros DROP CONSTRAINT describe_juros_pkey;
       public            samir    false    215            �           2606    16511    juros juros_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.juros
    ADD CONSTRAINT juros_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.juros DROP CONSTRAINT juros_pkey;
       public            samir    false    223            �           2606    16516 "   salario_minimo salario_minimo_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.salario_minimo
    ADD CONSTRAINT salario_minimo_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.salario_minimo DROP CONSTRAINT salario_minimo_pkey;
       public            samir    false    224            �           2606    16521     taxa_reajuste taxa_reajuste_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.taxa_reajuste
    ADD CONSTRAINT taxa_reajuste_pkey PRIMARY KEY (codigo);
 J   ALTER TABLE ONLY public.taxa_reajuste DROP CONSTRAINT taxa_reajuste_pkey;
       public            samir    false    225            �           2606    16523 7   describe_correcao_correcao uk_71gq90bfgcr920spupy3ok54n 
   CONSTRAINT     y   ALTER TABLE ONLY public.describe_correcao_correcao
    ADD CONSTRAINT uk_71gq90bfgcr920spupy3ok54n UNIQUE (correcao_id);
 a   ALTER TABLE ONLY public.describe_correcao_correcao DROP CONSTRAINT uk_71gq90bfgcr920spupy3ok54n;
       public            samir    false    221            �           2606    16455 +   describe_juros uk_c0kbjt2irij0hp22wa0cl8oa7 
   CONSTRAINT     f   ALTER TABLE ONLY public.describe_juros
    ADD CONSTRAINT uk_c0kbjt2irij0hp22wa0cl8oa7 UNIQUE (type);
 U   ALTER TABLE ONLY public.describe_juros DROP CONSTRAINT uk_c0kbjt2irij0hp22wa0cl8oa7;
       public            samir    false    215            �           2606    16447 .   describe_correcao uk_h8s4ifr5tagr9oiq356cdn4ci 
   CONSTRAINT     m   ALTER TABLE ONLY public.describe_correcao
    ADD CONSTRAINT uk_h8s4ifr5tagr9oiq356cdn4ci UNIQUE (describe);
 X   ALTER TABLE ONLY public.describe_correcao DROP CONSTRAINT uk_h8s4ifr5tagr9oiq356cdn4ci;
       public            samir    false    214            �           2606    16525 1   describe_juros_juros uk_jqjpurcnj9nrpkfiqymn6qy7c 
   CONSTRAINT     p   ALTER TABLE ONLY public.describe_juros_juros
    ADD CONSTRAINT uk_jqjpurcnj9nrpkfiqymn6qy7c UNIQUE (juros_id);
 [   ALTER TABLE ONLY public.describe_juros_juros DROP CONSTRAINT uk_jqjpurcnj9nrpkfiqymn6qy7c;
       public            samir    false    222            �           2606    16449 .   describe_correcao uk_lbjsmxxwv46hcvap2ik8pute0 
   CONSTRAINT     i   ALTER TABLE ONLY public.describe_correcao
    ADD CONSTRAINT uk_lbjsmxxwv46hcvap2ik8pute0 UNIQUE (type);
 X   ALTER TABLE ONLY public.describe_correcao DROP CONSTRAINT uk_lbjsmxxwv46hcvap2ik8pute0;
       public            samir    false    214            �           2606    16453 +   describe_juros uk_ni9e29llx1rh9arsmp87cuqa1 
   CONSTRAINT     j   ALTER TABLE ONLY public.describe_juros
    ADD CONSTRAINT uk_ni9e29llx1rh9arsmp87cuqa1 UNIQUE (describe);
 U   ALTER TABLE ONLY public.describe_juros DROP CONSTRAINT uk_ni9e29llx1rh9arsmp87cuqa1;
       public            samir    false    215            �           2606    16445    usuario usuario_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_pkey;
       public            samir    false    217            �           2606    16551 !   juros fk4uofo4ulgk30jkksqt3bdbdrj    FK CONSTRAINT     �   ALTER TABLE ONLY public.juros
    ADD CONSTRAINT fk4uofo4ulgk30jkksqt3bdbdrj FOREIGN KEY (describe_juros_id) REFERENCES public.describe_juros(id);
 K   ALTER TABLE ONLY public.juros DROP CONSTRAINT fk4uofo4ulgk30jkksqt3bdbdrj;
       public          samir    false    215    223    3231            �           2606    16546 0   describe_juros_juros fk7m6t7jl6vls7c2gifc64fd4ae    FK CONSTRAINT     �   ALTER TABLE ONLY public.describe_juros_juros
    ADD CONSTRAINT fk7m6t7jl6vls7c2gifc64fd4ae FOREIGN KEY (describe_juros_id) REFERENCES public.describe_juros(id);
 Z   ALTER TABLE ONLY public.describe_juros_juros DROP CONSTRAINT fk7m6t7jl6vls7c2gifc64fd4ae;
       public          samir    false    215    222    3231            �           2606    16536 6   describe_correcao_correcao fkcq2tno1p6swaww909cy89r1hg    FK CONSTRAINT     �   ALTER TABLE ONLY public.describe_correcao_correcao
    ADD CONSTRAINT fkcq2tno1p6swaww909cy89r1hg FOREIGN KEY (describe_correcao_id) REFERENCES public.describe_correcao(id);
 `   ALTER TABLE ONLY public.describe_correcao_correcao DROP CONSTRAINT fkcq2tno1p6swaww909cy89r1hg;
       public          samir    false    221    214    3225            �           2606    16526 $   correcao fkllnoyhg9bh43a5jw2v264riif    FK CONSTRAINT     �   ALTER TABLE ONLY public.correcao
    ADD CONSTRAINT fkllnoyhg9bh43a5jw2v264riif FOREIGN KEY (describe_correcao_type) REFERENCES public.describe_correcao(id);
 N   ALTER TABLE ONLY public.correcao DROP CONSTRAINT fkllnoyhg9bh43a5jw2v264riif;
       public          samir    false    220    214    3225            �           2606    16531 5   describe_correcao_correcao fknf2ropejw4f8f1nw89cmw357    FK CONSTRAINT     �   ALTER TABLE ONLY public.describe_correcao_correcao
    ADD CONSTRAINT fknf2ropejw4f8f1nw89cmw357 FOREIGN KEY (correcao_id) REFERENCES public.correcao(id);
 _   ALTER TABLE ONLY public.describe_correcao_correcao DROP CONSTRAINT fknf2ropejw4f8f1nw89cmw357;
       public          samir    false    221    3241    220            �           2606    16541 0   describe_juros_juros fkqf623fxih9apfni26t2ftrpep    FK CONSTRAINT     �   ALTER TABLE ONLY public.describe_juros_juros
    ADD CONSTRAINT fkqf623fxih9apfni26t2ftrpep FOREIGN KEY (juros_id) REFERENCES public.juros(id);
 Z   ALTER TABLE ONLY public.describe_juros_juros DROP CONSTRAINT fkqf623fxih9apfni26t2ftrpep;
       public          samir    false    223    222    3247            ]   �  x��Z�N�0>�Oa��]5?m��$,�qd'�Z�'��8����O�������
J�c�����şޝ�0�Ɍ,W�_A
!�ZȒ�T�R�K���e%i6�1���E�������'����e��B	X���h�S���HN����)�W��ğ����B�"��S��)M�-�2%[B!��K�/+^?i_`���)%$HG�w�L,�;
����U�z�q1{N�Nch���[���_z�ߔM��':��!����Y��?�O:ZX4$������1�&�ـ�bו��d^ =ڀ6"M�%���l�^�׽�4S?�,�*m	�/Q��?s��f�Q1[Ӓ�}r��]#�1�d��E��V/Fp��|[�9'���Ɗ_� �;Y��d��9A�/�g	��D�i���0*>�,2�wqieH�2*���tp�Q���Z/ؐ/��M����l��+ѷ�.�{��1;������P��J���ަb����R�����"�f�AӴ�1��k����v!~�Ux5|�����:���;Gq|��ȡ:��_�T{L�?��6���B�1�F���\���Y�ҐQ���U��6WmG��ש�Bs��Q}�t֊��n::.R�	↣�"�0��oJ�ƣ���`M;����n��ܛ��E}�N�wA[O��2���	��z ��?'��?ccE      O      x������ � �      P      x�e�]��D���2 ~��r���[Je"�����
˂������������ۿ�����>� ������?V����_�?�C��-�E:���%}�V�QHW�^A+��k'����6�FZI�d���!�p��k_�Ij��F�A)�`�d/�8�,��CCl��2m)�0�d��26@{P�i�m�!(CS^$}"���0�-R�?�|I��� �B�4L$穤LË4<F�4�q�F�4����2�R-��A���Mi��04������L�X���_mUR�?�2Bfa�V#e���	��� e?ښ�LBA�!�މ��*	10����/#�Y���G��=��_{����!�� 5o'e��q��w�"�}��[<�q����׍_bfbx�����k��ٽz1RU��ó��a�vRe�@qS��:H�x2+��i��!U�t�2m#��K��c���X=@�h�̪�2a}�����ĵL	��L	C���l'��!��.R&�.З��#f�TI��Z%U�F3��Xgf�t�['ef\;�*X�f�m�!���)OPĖK��%U�@[!�b ��*$HY3R}]Dk�J�:�ޠ��� ^GѰ6I�Y�oܞ���Dc�����b눭gA�3�Jj�o܍�yXq�F����NzTT��Z�S��ˈGxH׹�Ч8�k���O���Z����vp��Ю.�Ч��4�����4~
�)N[�x�Ч8U�NR}^��S�γ>�Oqz~^}�S}^��Ч8U=�_C�����}'c���Ƶ�m�X��ts�w�^��K�eXo}�`��Y8���g� ֋�kx��`��,��kY=��O� ~���+u`�� �� YB�UvU	�"��UX5 ���[G�UⲐ ��e%q\�O)�?��~�	�jA=�ݫ����|�*YO�U�� � �3�U�Z&��?5ű�ڭsevS�˪l?e�����SX�U�� �0diVe���Ґ�X�!��������`��,0��}Ya��O�?5x~P9wJt�E��� /qn�Ǉk~U�_��޾d|�r���ە\ݤ3%-0>_��-����TޛQj�bF��T�IDylsc��7,��W�G,7��Wq�0>c��m���;�dc�8>dϹ��Ļ����+�gY�����0Ne�o�d�{�����Z�����W7i���8��57��&v�u���I<��?���������o����$n��wͭo���t����0¯U�x��/Il��{�a���~�l�kn�y����|�E��D����}x�]p����x}�%��^��F��c/���Y��bo�AZ��L�cCn��*Ǝ��Wk�������텆�Ǵ#Ο���<&
ZI՛ă�ﱒ�$���rK��	��X�W��2L%���e�JJ�fnu��G�c���}��fw�&}��n7>}����؈籪F�ϵ߭,#6��]�� ~��A##�T�����3�ݙ<�cv�~���ǲ�}�ޕGʈ�V�-=X��+�P����7ң�B�[�{�x�[ w�y�	�Yf�WO�ńD�h��v�
�
����L�&�1!ZP�	Q����\-�W��@���G����L~��lF<���~�F��l��:+�ū1��q��Ϫ�'�G柪��ٕM^=��~�ٕu>�"��z�G���~1�x��[������F�Ο����=�����I� ��g՗A�'�#0�̮L�����&/�W�)+��/�Wb.���<K��Q���y�N��Z?��V��x���Q%��Qo�"F��NH��~�8�p%ng��s������%�Iո���0u���/�����rW�xc��i+0��~��2#�ձX�D/���݉���f�|ܗO�k���z���m�\i�Wď���}��G^v_��{�ՙE��'|�U"��K�O~�Ww�~���E7�����(���vm�__?��ׇ�bV�8"���(�3�>�]ey�Y1�}��/�z3b�����1y�f��C|�fq�W�Up���LY<�Jvf,@a'ˮ����c�RvN�&ǹ_�֌�?e�����_�֬�jFY��"��J�f�W/b��O��h�@�R���aJBSfg�����&$De�fq^aa*�5��U�:�/[��l͸�&cTF�d�{SÛ �l͸�W��&�BX��5��$h#�wGwbf$N�,���E�i�v�ؿ[hK�[Ƴo�v�s�i���v���ړx�0��L%,�e�gF|�C^�]Lf<_��<[����_�,&|q�`�< ~�Ufc���̳E.���-[��`X�<[��|�����3jPD�G����2%�x���Ob�7|��x\/vK�gw��n�����ּa�T�.%qf�J%~��%����� ���3�}*O�(�NI�M&����Rg��.�<Y��>�Ke|o|�Ke�*�A;6Le�2�?�]P�1~�bTF~���5�J�.�2�zZ����V5��٪q1}��(T��;$ӘK�� k�%�g�9�r� s��>Lk�Q�c�x����� �1D�8�)`�y�C7Ӽ���iM/W7ۚ&^�C��4�4��y7���1L������HO�� k�'�i�9ԓ5��q��1R�ٗ����K� k�%�j�5�r[����딄3Y�`��{��yNe]�5�����ǃ>9\� �i
`y�� k�^�����K���	P�����aN��k�Xs>9e�xj�'�l�5�s�7��>Ǡ0c<&m��1j���k�SZװ��br�XC19n����:��n�5�sL܀k�g}q����0g��`�yn3��c#[�(�&a�����a�K�09M�y���@L�� s"F5`�YךZ��ɑ`��35���������]��P�+��B�4b�Ʒ�r*���ʩ�WZ�}d�����W:+��^���R{��)c;��S�v���A�g�ķ�rJt�*�^�|�%ߕ�ʩtW�*��]�tjt�_�ʱdױ�wL�ul�2�CR9et��r*��ʩ��wx�A�T�+��S9�s/���N9��J9�T���e�$���rH��f�!�:Ŕc�u���}o-�T�眬����RN%���r(��Jʩ�V)��Z[H9��J�TJ��Q�������]�T���.ʩDOnd���rJ�s��E���Kf��ǈF��P��RB9��Z�/d�X���Rc�)�;�Sw�'��;�����}rJ�s�'�X鞜�_]��&}5���$�R<9��J��T�jk'�4W�ur���䔑���M�F���di"c@44��ǌn���f5�c��RY���嫔����RÀ��
dLKw)(�1�ީ�vq� z�����������':ȱ,Q� �rD)�>�J�rZT�S	�4AN�R9�?ȩ�Pj ��Ci����r*5�ȩ�P*�5��4@N�R 9�J��}��rBg��MN(����~����q*!��ǩ|P��m�A�}����q*��ǩ\����&�����Q>N%���8�J��t����@�A��.	��ǩ�9ܻ�v=%���q(���ǩ��!z����{~4�Sɟ�<N�~R�|�(?�ǩ�O*�?ix����Ǳ�ϸ.��9g�}����8��9�c9����Y����S��0;�%|R�8���T�'��Sɞ�:N�zR�8��9��c��T:N%z��8��I��4O���G�8��I��T�'e�Sʏ��8��I��T��lnb��9N�w��8��I��Kr'-�S���8N�v��8�ٹ��-��Sy�8N�<��sE��q*���C)��7�J餻q*���Ʃ�N���9[�8��Io�6��6N%s��+�s�db��v6Ner�kc��66N�q��q(���Ʃ,N���8ik��᤬q*����)Ρj�Jऩq*�E�Vz���8��IM�T�&-�S���4N�nG�X�&�S��44N�mR�8��I?����3j�6ig����N1t��6�f�Jؤ�q*_�bƩtMz��5�e�J֤�q*W�R�C�T�{F�hj%�T�&��Sy�2Ni/�T�f V   ��r4ic��Ѥ�q*Es��F��9T�w�41N�g�X~��0��g�Tr�
��ͤ�q�{�Ʃ�̡`�̴3��������w�      J   �   x��Ͽ�0�>�
�"�ٚRMI������qp7��=��bv�����}<(e�I��k獮Zٿ���J�ҹ)�]C��A�O�:;@EcqDX!x�8ۍ�"��"�kZ.��ΟdK�I��>]"�Z�n�j5a:`6��ͤ;�N��*��$L��R�m�
o��#����P���Y8uN�%b�} Sn�b      Q      x������ � �      K   �   x�]OK
�0]7�j���*��6B���~V�ą�%�Eq�r��b�-"��7��LIP=4���;mW�1<���+^��n��po���
����ڻt�PI�v���$c�=vq���j`�s����ހ
#F/�����bSN���j�5�4\��𐌖,��X���d[EI��r��#FN��,7`�      R      x������ � �      S   �  x�e�Q��D�;{y��Hj/o��U�K0_��A�j'*tdj������\���d� Sb�3�A��&� 3b�_�X�EL7�FBB==�����9�� #߾��w40�F�C��wt0�c3��`���a��0>���w60�F�S��wv0��|�#�i����Nx��L?|���90�5#_�`�k�|m���������f�k�`_��zz���
��:�� #_�`��F��`��F����7�f��`߀�Fz��7��`�F��`�F����w50�]F�K7c��]�`�5���v�.�sF�+��w-���Z3b��,�u�EllF���A���A���C��Z�����4�AL��W�|����0�	F�b�����}�+顇�60:�F��`��|u���N0�U#_u0��،}�����==�����u0���|�#�n`���|{��o_`�;�f�;����;���W6��`�;�|����0��|g#�)`�;u3���`�	���q�+����|g���\`�k�|M����|��������������8��Y��F������|]���;�� #_�`���:<������Ǿ�h`t.�|C��7:�� #ߘ`�F��`���<�w��}Wz�ʖ��F�k���Ϗ}e�>?������W���c_پϏ}��>?��7x��7x������W���c_��Ϗ}��>?������W���c_����}��ͤ�F��CtT7�����~(���C�Nro���y�A�Att�N�y�˽�*�Gy�K+=�]/������҃���A�v��w�H�!o}��C��"����EJy�����)=��/Rz� Rz� Rz� Rz�p;{�p;{�- Zz�5 Zz�= Zz�E Zz�M Zz�U ��0|�FNѧ��C��!@cޯp(d������7��]ː��o0a{7�)ܟr�@���#�5F&�Y~�\�߮n��߮� �]��<g�ON���18��A�|xrh�(ON��ɱ!�<<97d��'������Q�D9:d�7Q��M��CF�!���҃�����v��Df�!'���C�����!2K9Dd�r��,=��Yz�9"����Df�!'���C���!�v���D����D����D����D���E���E���#E���3E���CE���SE����v���v���E��!r���=D����"~�9\��"����C�x?{��/�g�F��!r���[-9��Q|b�@��}�t�����؁"�th���?T�����]4L���j��� j���OUo �7������z�z����A��!�]��~���g�v�'���j��w�����C��o�;x������+���������Y��Y3C�5p�J�502�J��2Xf	r#�ms�e Е�@D:t%2�[-���;�`~w�0��z�4���W,����]��Y�����/s���F�%}7�L�Su�������W���|Z���~>�� ��$�\KZ&q�Z�2�#ג�I���L�ȵ�Z>�s-��O�\K���;גj��ε�z�bap;n�p��X�Z�^zȵ����k�Pn�l���G�¿��|�Y�����8��~��g���N>�E˿�e�5_L/\2�-�X�w�Ťq�������~oع���L8��γ��*���"~�y>�4��E���������      T   �   x�=�A��0D�u|�nQ������"f�,�'��##�d����^_ρ�}5�.$'�G�)�n�Ғ�mʹZ��2���6M���IC[��E�kZ���e���M�8�eN���ھs�ʒ�m���阴���C�v;p:@3�Yv m�_�zm+�_��̓��k���X[�;��Ż3;��r�@
;m�h�@�e�ء��I���eD�hס�G
S��)l��;wA��w���p*      U   �  x�e�I�$�DǙ{�:�������
�P���uGFw�v�w���
��_����.S����>P~��~@������������hzPt=h�����:O�����[x}a+������Ū��s�j��\�JGdsq�i=��7���/�ɜ����L��E�f2�[!|3����ͤW8�G&m��Ȥ�XU�L:1��/;��·�Up	�L0Y��� ^|+�7�җc�L��4��4�U�X&{;n±L:q?�-��*2"�I�"�$f��E�M��ok��^�#��C�FR���pDR��pD2<�ބ#���]82Y>9}G&L�σ_&�;V�/��s�U��ļ��*w��)D_$>�a�/ߨ�/�h�/���H:W��7���^��<���o$��x�_$˓�*2�&>��*_$˓���J)i�pܳ
[Nj6ш���f�H6�8�#��g̜�q�p������j�p&�;YE8"�>}g	| K5�D���j|g����D:_�E�.�}��!�p�)g	�YK��%N��M���.�7�ս�����N�����gtvX�t߾Uo�Ӌg�U�Kg��k��e�)�Ӵ�p���z�S��\G:�CX��Zvl��\����*|�e�݄���Q����i���t���^��{T�sp����|�a[�W�E8n�ϕ����3�ڄ���U8����	Ǳ��\>���}����H����T��Q V�>c�-���±f>{�M��� �±f�gbM�f2*�.�G@��8y[a�)���%��\�n��/��e��U��`&'����r�h*��)��a�*����M���~���y������bl��c����[<���KmE|�p�an̦R_4}�{�J}�v�J���q���f�<�!~���|�?�u�D����[x|��p����&�R|>{=8B1f�U���U$���	v֘,ܣ}
��e/��G\�}��$F�y4n��X)��M8V
���O�6�~�~��W�P���<����r:a��q�x�F�:��<�g�C8�ఏ��;�±B&;i�7W����k�G����QR\,��"�f�XUF$�=b��3�!|[O���Qr�Y}�ǌ��Nn�cF�qG�*�e�ݾJ�e��43J���W�1�丝w�Q�丝W�V���!/ȭ*#�ƹ�!9�hl]�-�Qч�$���.z#iŇMB�8�)|#��7.�X&���!$���6�J�9�1~;�O�*�-�x���p��㎲w�H�l�C8����u�K���N�h$�qRIr��� $�6��8~ҁ�$��SP�/	��� $U�.n� !�jg� $Um���q<��.wP�`i��^OU?g [׃߼;l���i?�T����� �i�^�_r�t�Ƭ�r�����s��Mm��� ������My0�փ��G(=Hnjnɠ����k�*2E��*2E¾Yer�a�����<h�l�qSy������x�ܴ�o
�����L���Sx��V`�!��W΃_&�XU�L��Ue:�o���׳��;HN��.i<HJ�S�$�c��&W�[(<�J�=W
��r�Px�?�v�%�>���}pR/�eCE>���*2]�NYc�Q�|�� ��6�#������)@���;��L�	^�7���[�FR�B��F�֦� �(USUF$���XU�H�3L�A��Z��.��{�΃�͕t�������̕ʃ����ʃ��4!Py0s$^;��G���׃_$\%KE�Hx�-�"�C�A���8�)�p|�h<H>Z�Jh<H>j��4$�i��/@�ArS�=����l�c?�M7ѱ�������[+'��g��IZ�����7��IZʝb�|���Ϩ�IZj�J;擴���O�R����>IKy��Q���8������V���]he�?����Y U����;�]��h�����},ii��bt�^�q�},yi_مC�][��c�K��~�D�J���o�~���J��O��J���#1V&~2��ʛ��v�Y%^&{7�z���F�$��pTC8���G�ф,Y�`�T!K��/F�d��ä	Y���uG�$�6}ZiB�%�o�*8ي��&d��k��]8l�3B����>�&dIi������r$[8]��&dIi)D�PٞEȒў@2˲�4����n*2����������"Yq      M      x������ � �     