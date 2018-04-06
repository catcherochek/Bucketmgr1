<?php //ВСЕ ПЕРЕДЕЛАННЫЕ ВЫБОРКИ, НУЖНЫЕ МНЕ ЧЕРЕЗ ODBC СОЕДИНЕНИЕ
define('GETMANUFACTURERS','select MFA_ID As ID, MFA_BRAND AS BRAND from TOF_MANUFACTURErS where (MFA_PC_CTM subrange(187 cast integer)) = 1 ORDER by MFA_BRAND');
function GETMARKS($cartypeID){
	$val = <<<EOT
	SELECT coalesce("LNG_TEX"."TEX_TEXT", "UNI_TEX"."TEX_TEXT") vehicle_des,
         coalesce("TYC_PCON_START", "TYP_PCON_START") pcon_start,   
         coalesce("TYC_PCON_END", "TYP_PCON_END") pcon_end,   
         coalesce("TYC_KW_FROM", "TYP_KW_FROM") kw_from,   
         coalesce("TYC_HP_FROM", "TYP_HP_FROM") hp_from,   
         coalesce("TYC_CCM", "TYP_CCM") ccm,   
         coalesce("TYC_BOD_TEX"."TEX_TEXT", "BOD_TEX"."TEX_TEXT", "TYC_MOD_TEX"."TEX_TEXT", "MOD_TEX"."TEX_TEXT") "body",   
         coalesce("TYC_AXL_TEX"."TEX_TEXT", "AXL_TEX"."TEX_TEXT") axis,   
         coalesce("TYC_MAX_WEIGHT", "TYP_MAX_WEIGHT") max_weight,   
         coalesce("TYC_KV_BODY_DES_ID", "TYP_KV_BODY_DES_ID", "TYC_KV_MODEL_DES_ID" , "TYP_KV_MODEL_DES_ID" ) body_des_id,
         coalesce("TYC_KV_ENGINE_DES_ID", "TYP_KV_ENGINE_DES_ID") engine_des_id,   
         coalesce("TYC_KV_AXLE_DES_ID", "TYP_KV_AXLE_DES_ID") axis_des_id,   
         "TYP_MOD_ID" mod_id,   
         "TYP_ID" typ_id,   
         '              ' button, 
         TYP_LA_CTM subrange(225 CAST INTEGER) flag_id,
         MOD_PC,
         MOD_CV,
         MOD_MFA_ID,
            TYP_KV_FUEL_DES_ID,
         typ_sort,
            coalesce(short_tex.tex_text, uni_short_tex.tex_text) as short_des,
            findbit(lng_des.cds_ctm) as filter_cou_id
    FROM tof_types
    JOIN TOF_MODELS
      ON "TYP_MOD_ID" = MOD_ID
    LEFT OUTER JOIN "TOF_DESIGNATIONS" "MODEL_DES"
                 ON "TOF_TYPES"."TYP_KV_MODEL_DES_ID" = "MODEL_DES"."DES_ID" 
                AND MODEL_DES.DES_LNG_ID = 16
    LEFT OUTER JOIN TOF_DES_TEXTS MOD_TEX
                 ON MODEL_DES.DES_TEX_ID = MOD_TEX.TEX_ID
    LEFT OUTER JOIN "TOF_DESIGNATIONS" "AXLE_DES"
                 ON "TOF_TYPES"."TYP_KV_AXLE_DES_ID" = "AXLE_DES"."DES_ID" 
                AND AXLE_DES.DES_LNG_ID = 16
    LEFT OUTER JOIN TOF_DES_TEXTS AXL_TEX
                 ON AXLE_DES.DES_TEX_ID = AXL_TEX.TEX_ID
    LEFT OUTER JOIN "TOF_DESIGNATIONS" "BODY_DES"
                 ON "TOF_TYPES"."TYP_KV_BODY_DES_ID" = "BODY_DES"."DES_ID" 
                AND BODY_DES.DES_LNG_ID = 16
    LEFT OUTER JOIN TOF_DES_TEXTS BOD_TEX
                 ON BODY_DES.DES_TEX_ID = BOD_TEX.TEX_ID
    LEFT OUTER JOIN TOF_COUNTRY_DESIGNATIONS LNG_DES
                 ON TYP_MMT_CDS_ID = LNG_DES.CDS_ID
                AND LNG_DES.CDS_LNG_ID = 16
                AND LNG_DES.CDS_CTM subrange(225 CAST INTEGER) = 1
    LEFT OUTER JOIN TOF_DES_TEXTS LNG_TEX
                 ON LNG_DES.CDS_TEX_ID = LNG_TEX.TEX_ID
    LEFT OUTER JOIN TOF_COUNTRY_DESIGNATIONS UNI_DES
                 ON TYP_MMT_CDS_ID = UNI_DES.CDS_ID
                AND UNI_DES.CDS_LNG_ID = 255
                AND UNI_DES.CDS_CTM subrange(225 CAST INTEGER) = 1
    LEFT OUTER JOIN TOF_DES_TEXTS UNI_TEX
                 ON UNI_DES.CDS_TEX_ID = UNI_TEX.TEX_ID
    LEFT OUTER JOIN "TOF_TYP_COUNTRY_SPECIFICS"
                 on TYP_ID = TYC_TYP_ID
                and TYC_COU_ID = 225
    LEFT OUTER JOIN "TOF_DESIGNATIONS" "TYC_MODEL_DES"
                 ON "TYC_KV_MODEL_DES_ID" = "TYC_MODEL_DES"."DES_ID" 
                AND TYC_MODEL_DES.DES_LNG_ID = 16
    LEFT OUTER JOIN TOF_DES_TEXTS TYC_MOD_TEX
                 ON TYC_MODEL_DES.DES_TEX_ID = TYC_MOD_TEX.TEX_ID
    LEFT OUTER JOIN "TOF_DESIGNATIONS" "TYC_AXLE_DES"
                 ON "TYC_KV_AXLE_DES_ID" = "TYC_AXLE_DES"."DES_ID" 
                AND TYC_AXLE_DES.DES_LNG_ID = 16
    LEFT OUTER JOIN TOF_DES_TEXTS TYC_AXL_TEX
                 ON TYC_AXLE_DES.DES_TEX_ID = TYC_AXL_TEX.TEX_ID
    LEFT OUTER JOIN "TOF_DESIGNATIONS" "TYC_BODY_DES"
                 ON "TYC_KV_BODY_DES_ID" = "TYC_BODY_DES"."DES_ID" 
                AND TYC_BODY_DES.DES_LNG_ID = 16
    LEFT OUTER JOIN TOF_DES_TEXTS TYC_BOD_TEX
                 ON TYC_BODY_DES.DES_TEX_ID = TYC_BOD_TEX.TEX_ID

    left outer join tof_country_designations short_des
                  on typ_cds_id = short_des.cds_id
                  and short_des.cds_ctm subrange(findbit(lng_des.cds_ctm) cast Integer) = 1
                  and short_des.cds_lng_id = 16
     left outer join tof_des_texts short_tex
                  on short_tex.tex_id = short_des.cds_tex_id

    left outer join tof_country_designations uni_short_des
                  on typ_cds_id = uni_short_des.cds_id
                  and uni_short_des.cds_ctm subrange(findbit(lng_des.cds_ctm) cast Integer) = 1
                  and uni_short_des.cds_lng_id = 255
     left outer join tof_des_texts uni_short_tex
                  on uni_short_tex.tex_id = uni_short_des.cds_tex_id

   WHERE typ_mod_id = {$cartypeID} 
     AND typ_ctm subrange(225 CAST INTEGER) = 1
EOT;
   return $val;
}//kategorii avto
function GETAUTO($mfa_id){
	$val = <<<EOT
	SELECT nvl(LNG_TEX.tex_text, UNI_TEX.tex_text) bez,   
         tof_models.mod_id,   
         tof_models.mod_pc,   
         tof_models.mod_cv,
         mod_pcon_start,
         mod_pcon_end,
		mod_mfa_id
    FROM tof_models
    LEFT OUTER JOIN TOF_COUNTRY_DESIGNATIONS UNI_DES
      ON MOD_CDS_ID = UNI_DES.CDS_ID
     AND UNI_DES.CDS_LNG_ID = 255
     AND UNI_DES.CDS_CTM Subrange(225 cast integer) = 1 

    LEFT OUTER JOIN TOF_DES_TEXTS UNI_TEX
      ON UNI_DES.CDS_TEX_ID = UNI_TEX.TEX_ID   

    LEFT OUTER JOIN TOF_COUNTRY_DESIGNATIONS LNG_DES
      ON MOD_CDS_ID = LNG_DES.CDS_ID
     AND LNG_DES.CDS_LNG_ID = 16
     AND LNG_DES.CDS_CTM Subrange(225 cast integer) = 1

    LEFT OUTER JOIN TOF_DES_TEXTS LNG_TEX
      ON LNG_DES.CDS_TEX_ID = LNG_TEX.TEX_ID   

   WHERE mod_mfa_id = {$mfa_id}
			and (   (     1 = 1 and 1 = 1 
                   and ( tof_models.mod_pc = 1 or tof_models.mod_cv = 1 )
                   and ( mod_pc_ctm subrange(225 CAST INTEGER) = 1 OR mod_cv_ctm subrange(225 CAST INTEGER) = 1)
                 )
				  or ( 1 = 0 and 1 = 1 and ( tof_models.mod_cv = 1 ) and mod_cv_ctm subrange(225 CAST 

INTEGER) = 1)
				  or ( 1 = 1 and 1 = 0 and ( tof_models.mod_pc = 1 ) and mod_pc_ctm subrange(225 CAST 

INTEGER) = 1)
				 )
			
			and (   (0 = 1 and (    mod_id in (select fil_value
													     from 

tof_filters
													  join tof_models 

mod1
														 on 

fil_value = mod1.mod_id 
													 where fil_kind = 2 
														and 

fil_uss_id = 1 )
            							or not exists (select 1 
													  from tof_filters
													  join tof_models 

mod2
														 on 

fil_value = mod2.mod_id 
													 where fil_kind = 2 
														and 

fil_uss_id = 1 
														and (    ( 

mod2.mod_pc = 1 and mod2.mod_pc_ctm subrange(225 CAST INTEGER) = 1 ) 
															   

or ( mod2.mod_cv = 1 and mod2.mod_cv_ctm subrange(225 CAST INTEGER) = 1 )
                                                                      ) 
														and 

mod2.mod_mfa_id = {$mfa_id} )
															
										 ) 
                  )
               or (0 = 0 )
              )
ORDER by bez
EOT;
return $val;
	
}
function GETPARTTREE($id){
	$val = <<<EOT
	select str_id,
       str_level,
       str_sort,
       0 expand,
       tex_text,
       str_id_parent,
       0 color
  from tof_search_tree
  join tof_designations
    on str_des_id nljoin des_id and
       des_lng_id = 16
  join tof_des_texts
    on des_tex_id nljoin tex_id
 where 1 < 3 and
       1 = 1 and
       str_type = 1 and
       str_level > 1 and
       not exists (select 1
                     from tof_search_tree_filters
                    where stf_uss_id = 1 and
                          stf_str_id = str_id) and
       (select nvl(max(lgs_ga_id), 0)
                 from tof_link_ga_str
                 join tof_link_la_typ
                   on lgs_ga_id nljoin lat_ga_id and
                      lat_typ_id = {$id} and
                      lat_ctm subrange(225 cast integer) = 1
                where lgs_str_id = str_id and
                      (-1 < 0 or lgs_ga_id in (-1))) > 0

UNION ALL
select str_id,
       str_level,
       str_sort,
       0 expand,
       tex_text,
       str_id_parent,
       0 color
  from tof_search_tree
  join tof_designations
    on str_des_id nljoin des_id and
       des_lng_id = 16
  join tof_des_texts
    on tex_id = des_tex_id
 where 1 = 2 and
         1 = 6 and
       str_type = 1 and
       str_level > 1 and
       not exists (select 1
                     from tof_search_tree_filters
                    where stf_uss_id = 1 and
                          stf_str_id = str_id) and
       (select nvl(max(lgs_ga_id), 0)
                 from tof_link_ga_str
                 join tof_link_la_mrk
                   on lam_ga_id = lgs_ga_id and
                      lam_mrk_id = {$id} and
                      lam_ctm subrange(225 cast integer) = 1
                where lgs_str_id = str_id and
                      (-1 < 0 or lgs_ga_id in (-1))) > 0

UNION ALL
select str_id,
       str_level,
       str_sort,
       0 expand,
       tex_text,
       str_id_parent,
       0 color
  from tof_search_tree
  join tof_designations
    on str_des_id nljoin des_id and
       des_lng_id = 16
  join tof_des_texts
    on tex_id = des_tex_id
 where 1 = 3 and
       str_type = 1 and
       str_level > 1 and
       not exists (select 1
                     from tof_search_tree_filters
                    where stf_uss_id = 1 and
                          stf_str_id = str_id) and
       (select nvl(max(lgs_ga_id), 0)
                 from tof_link_ga_str
                 join tof_link_la_eng
                   on lae_ga_id = lgs_ga_id and
                      lae_eng_id = {$id} and
                      lae_ctm subrange(225 cast integer) = 1
                where lgs_str_id = str_id and
                      (-1 < 0 or lgs_ga_id in (-1))) > 0
UNION ALL
select str_id,
       str_level,
       str_sort,
       0 expand,
       tex_text,
       str_id_parent,
       0 color
  from tof_search_tree
  join tof_designations
    on str_des_id nljoin des_id and
       des_lng_id = 16
  join tof_des_texts
    on tex_id = des_tex_id
 where 1 = 5 and
       str_type = 1 and
       str_level > 1 and
       not exists (select 1
                     from tof_search_tree_filters
                    where stf_uss_id = 1 and
                          stf_str_id = str_id) and
       (select nvl(max(lgs_ga_id), 0)
                 from tof_link_ga_str
                 join tof_link_la_axl
                   on laa_ga_id = lgs_ga_id and
                      laa_axl_id = {$id} and
                      laa_ctm subrange(225 cast integer) = 1
                where lgs_str_id = str_id and
                      (-1 < 0 or lgs_ga_id in (-1))) > 0
UNION ALL
select str_id,
       str_level,
       str_sort,
       0 expand,
       tex_text,
       str_id_parent,
       2 color
  from tof_search_tree
  join tof_designations
    on str_des_id nljoin des_id and
       des_lng_id = 16
  join tof_des_texts
    on des_tex_id nljoin tex_id
 where 1 = 4 and
       str_type = 1 and
       str_level > 1 and
       not exists (select 1
                     from tof_search_tree_filters
                    where stf_uss_id = 1 and
                          stf_str_id = str_id) and
       (select nvl(max(lgs_ga_id), 0 )
          from tof_link_ga_str
          join tof_link_art_ga
                on lgs_ga_id nljoin lag_ga_id
           join tof_articles
            on lag_art_id nljoin art_id and
               art_ctm subrange(225 cast integer) = 1
          join tof_generic_articles
            on ga_id = lgs_ga_id and
               ga_universal = 1
         where lgs_str_id = str_id and
               (-1 < 0 or lgs_ga_id in (-1)) )> 0
          ORDER BY str_sort asc
EOT;
    return $val;
};//derevo s zapchastyami
function GETTREEINFO($carID,$catID){
	$val = <<<EOT
select distinct lat_sup_id sup_id,

                      nvl(sup_cou.sup_brand, sup_null.sup_brand) supplier,

                     ga_nr,

                      tex_text masterbez,

                      'column' col,

                     nvl(trf_equal.trf_abc, trf_null.trf_abc) trf_abc,

                     nvl(trf_equal.trf_sort, trf_null.trf_sort) trf_sort,

                      1 firstgr,

                      1 lastgr,

                0 special_filter,

                '' sup_ids

                from tof_link_la_typ

                join tof_generic_articles

                 on ga_id = lat_ga_id 

                join tof_designations

                 on des_id = ga_des_id and

                    des_lng_id = 16

                join tof_des_texts

                 on tex_id = des_tex_id

left outer join tof_suppliers sup_cou

                 on sup_cou.sup_id = lat_sup_id and

                     sup_cou.sup_cou_id = 225

left outer join tof_suppliers sup_null

                 on sup_null.sup_id = lat_sup_id and

                     sup_null.sup_cou_id is null

left outer join tof_retail_filters trf_equal

                 on trf_equal.trf_ga_id = ga_id and

                     trf_equal.trf_sup_id = lat_sup_id and

                     trf_equal.trf_tsd_id IS NULL

left outer join tof_retail_filters trf_null

                 on trf_null.trf_ga_id is null and

                     trf_null.trf_sup_id = lat_sup_id and

                     trf_null.trf_tsd_id IS NULL

              where lat_typ_id = {$carID}

               and lat_ctm subrange(225 cast integer) = 1

               and 1 < 3 and    

                     1 = 1 and    

                      (-1 < 0 or lat_ga_id in (-1)) and

                       lat_ga_id in ( select lgs_ga_id

                                         from tof_link_ga_str

                                           where lgs_str_id = {$catID})



UNION

            select distinct lam_sup_id sup_id,

                      nvl(sup_cou.sup_brand, sup_null.sup_brand) supplier,

                     ga_nr,

                      tex_text masterbez,

                      'column' col,

                     nvl(trf_equal.trf_abc, trf_null.trf_abc) trf_abc,

                     nvl(trf_equal.trf_sort, trf_null.trf_sort) trf_sort,

                      1 firstgr,

                      1 lastgr,

                0 special_filter,

                '' sup_ids

                from tof_link_la_mrk

                join tof_generic_articles

                 on ga_id = lam_ga_id 

                join tof_designations

                 on des_id = ga_des_id and

                    des_lng_id = 16

                join tof_des_texts

                 on tex_id = des_tex_id

left outer join tof_suppliers sup_cou

                 on sup_cou.sup_id = lam_sup_id and

                     sup_cou.sup_cou_id = 225

left outer join tof_suppliers sup_null

                 on sup_null.sup_id = lam_sup_id and

                     sup_null.sup_cou_id is null

left outer join tof_retail_filters trf_equal

                 on trf_equal.trf_ga_id = ga_id and

                     trf_equal.trf_sup_id = lam_sup_id and

                     trf_equal.trf_tsd_id IS NULL

left outer join tof_retail_filters trf_null

                 on trf_null.trf_ga_id is null and

                     trf_null.trf_sup_id = lam_sup_id and

                     trf_null.trf_tsd_id IS NULL

              where lam_mrk_id = {$carID}

               and lam_ctm subrange(225 cast integer) = 1

               and 1 = 2 and    

                     1 = 6 and    

                      (-1 < 0 or lam_ga_id in (-1)) and

                       lam_ga_id in ( select lgs_ga_id

                                         from tof_link_ga_str

                                           where lgs_str_id = {$catID})



UNION

            select distinct lae_sup_id sup_id,

                      nvl(sup_cou.sup_brand, sup_null.sup_brand) supplier,

                     ga_nr,

                      tex_text masterbez,

                      'column' col,

                     nvl(trf_equal.trf_abc, trf_null.trf_abc) trf_abc,

                     nvl(trf_equal.trf_sort, trf_null.trf_sort) trf_sort,

                      1 firstgr,

                      1 lastgr,

                0 special_filter,

                '' sup_ids

                from tof_link_la_eng

                join tof_generic_articles

                 on ga_id = lae_ga_id 

                join tof_designations

                 on des_id = ga_des_id and

                    des_lng_id = 16

                join tof_des_texts

                 on tex_id = des_tex_id

left outer join tof_suppliers sup_cou

                 on sup_cou.sup_id = lae_sup_id and

                     sup_cou.sup_cou_id = 225

left outer join tof_suppliers sup_null

                 on sup_null.sup_id = lae_sup_id and

                     sup_null.sup_cou_id is null

left outer join tof_retail_filters trf_equal

                 on trf_equal.trf_ga_id = ga_id and

                     trf_equal.trf_sup_id = lae_sup_id and

                     trf_equal.trf_tsd_id IS NULL

left outer join tof_retail_filters trf_null

                 on trf_null.trf_ga_id is null and

                     trf_null.trf_sup_id = lae_sup_id and

                     trf_null.trf_tsd_id IS NULL

              where lae_eng_id = {$carID}

               and lae_ctm subrange(225 cast integer) = 1

               and 1 = 3 and        

                      (-1 < 0 or lae_ga_id in (-1)) and

                       lae_ga_id in ( select lgs_ga_id

                                         from tof_link_ga_str

                                           where lgs_str_id = {$catID})

UNION

            select distinct laa_sup_id sup_id,

                      nvl(sup_cou.sup_brand, sup_null.sup_brand) supplier,

                     ga_nr,

                      tex_text masterbez,

                      'column' col,

                     nvl(trf_equal.trf_abc, trf_null.trf_abc) trf_abc,

                     nvl(trf_equal.trf_sort, trf_null.trf_sort) trf_sort,

                      1 firstgr,

                      1 lastgr,

                0 special_filter,

                '' sup_ids

                from tof_link_la_axl

                join tof_generic_articles

                 on ga_id = laa_ga_id 

                join tof_designations

                 on des_id = ga_des_id and

                    des_lng_id = 16

                join tof_des_texts

                 on tex_id = des_tex_id

left outer join tof_suppliers sup_cou

                 on sup_cou.sup_id = laa_sup_id and

                     sup_cou.sup_cou_id = 225

left outer join tof_suppliers sup_null

                 on sup_null.sup_id = laa_sup_id and

                     sup_null.sup_cou_id is null

left outer join tof_retail_filters trf_equal

                 on trf_equal.trf_ga_id = ga_id and

                     trf_equal.trf_sup_id = laa_sup_id and

                     trf_equal.trf_tsd_id IS NULL

left outer join tof_retail_filters trf_null

                 on trf_null.trf_ga_id is null and

                     trf_null.trf_sup_id = laa_sup_id and

                     trf_null.trf_tsd_id IS NULL

              where laa_axl_id = {$carID}

               and laa_ctm subrange(225 cast integer) = 1

               and 1 = 5 and        

                      (-1 < 0 or laa_ga_id in (-1)) and

                       laa_ga_id in ( select lgs_ga_id

                                         from tof_link_ga_str

                                           where lgs_str_id = {$catID})

UNION

            select distinct art_sup_id sup_id,

                      nvl(sup_cou.sup_brand, sup_null.sup_brand) supplier,

                     ga_nr,

                      tex_text masterbez,

                      'column' col,

                     nvl(trf_equal.trf_abc, trf_null.trf_abc) trf_abc,

                     nvl(trf_equal.trf_sort, trf_null.trf_sort) trf_sort,

                      1 firstgr,

                      1 lastgr,

                0 special_filter,

                '' sup_ids

                from tof_generic_articles

                join tof_designations

                 on des_id = ga_des_id and

                    des_lng_id = 16

                join tof_des_texts

                 on tex_id = des_tex_id

                join tof_link_art_ga

                 on lag_ga_id = ga_id

                join tof_articles

                 on art_id = lag_art_id and

                      art_ctm subrange(225 cast integer) = 1

left outer join tof_suppliers sup_cou

                 on sup_cou.sup_id = art_sup_id and

                     sup_cou.sup_cou_id = 225

left outer join tof_suppliers sup_null

                 on sup_null.sup_id = art_sup_id and

                     sup_null.sup_cou_id is null

left outer join tof_retail_filters trf_equal

                 on trf_equal.trf_ga_id = ga_id and

                     trf_equal.trf_sup_id = art_sup_id and

                     trf_equal.trf_tsd_id IS NULL

left outer join tof_retail_filters trf_null

                 on trf_null.trf_ga_id is null and

                     trf_null.trf_sup_id = art_sup_id and

                     trf_null.trf_tsd_id IS NULL

             where 1 = 4 and        

                      ga_universal = 1 and

                      (-1 < 0 or ga_id in (-1)) and

                       ga_id in ( select lgs_ga_id

                                       from tof_link_ga_str

                                     where lgs_str_id = {$catID})	
	
EOT;
	return $val;
}
function GETITEMFROMGROUP($carID,$suppID,$gaID){
	$val = <<<EOT
  select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text||' ('||ga_assembly_tex.tex_text||')' ga_assembly,
                     la_id,
                     lat_sort sort,
                     lat_sup_id bra_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) bra_brand,
                     COALESCE( (select sba_quantity
                                     from tof_shopping_baskets
                                         where sba_art_id = art_id and
                                               sba_uss_id = 1),
                                     0) basket,
                     COALESCE( (select sli_art_id
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 
                                     0) list,
                     NVL( (select 1
                                  from dual
                               where exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                              aun_uss_id = 1 and
                                                          aun_kind = 'G') or
                                      exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                          aun_uss_id = 1 and
                                                          aun_kind = 'S' and
                                                          aun_typ_id = {$carID})), 0) notice,
                acs_kv_status art_kv_status,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
               from tof_link_la_typ
              join tof_generic_articles
             on lat_ga_id nljoin ga_id
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     16 nljoin ga_des.des_lng_id
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id 
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
                join tof_link_art
                 on lat_la_id nljoin la_id
               join tof_articles
                 on la_art_id nljoin art_id and
                     art_ctm subrange(225 cast integer) = 1
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id  and
                     16 nljoin art_des.des_lng_id
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     255 nljoin art_des_uni.des_lng_id
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id
left outer join tof_suppliers sup_cou
                 on lat_sup_id nljoin sup_cou.sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on lat_sup_id nljoin sup_null.sup_id 
            and sup_null.sup_cou_id is null
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where lat_ga_id in ({$gaID}) and
                     lat_ctm subrange(225 cast integer) = 1 and
                     lat_typ_id = {$carID} and
                     lat_sup_id in ({$suppID}) and
                     (1 = 1)
UNION
              select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text||' ('||ga_assembly_tex.tex_text||')' ga_assembly,
                     la_id,
                     lae_sort sort,
                     lae_sup_id bra_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) bra_brand,
                     COALESCE( ( select count(*)
                                      from tof_shopping_baskets
                                          where sba_art_id = art_id and
                                                sba_uss_id = 1), 0) basket,
                     COALESCE( (select count(*)
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 0) list,
                     NVL( (select 1
                                  from dual
                               where exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                              aun_uss_id = 1 and
                                                          aun_kind = 'G') or
                                      exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                          aun_uss_id = 1 and
                                                          aun_kind = 'S' and
                                                          aun_eng_id = {$carID})), 0) notice,
                acs_kv_status art_kv_status,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
               from tof_link_la_eng
               join tof_generic_articles
                 on lae_ga_id nljoin ga_id
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     16 nljoin ga_des.des_lng_id
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
                join tof_link_art
                 on lae_la_id nljoin la_id
               join tof_articles
                 on la_art_id  nljoin art_id and
                     art_ctm subrange(225 cast integer) = 1
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id and
                     16 nljoin art_des.des_lng_id 
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id 
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     255 nljoin art_des_uni.des_lng_id
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id 
left outer join tof_suppliers sup_cou
                 on lae_sup_id nljoin sup_cou.sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on lae_sup_id nljoin sup_null.sup_id and
                     sup_null.sup_cou_id is null
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where lae_ga_id in ({$gaID}) and
                     lae_sup_id in ({$suppID}) and
                     lae_ctm subrange(225 cast integer) = 1 and
                     lae_eng_id = {$carID} and
                     (1 = 2)
UNION
              select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text||' ('||ga_assembly_tex.tex_text||')' ga_assembly,
                     0 la_id,
                     1 sort,
                     art_sup_id bra_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) bra_brand,
                     COALESCE( ( select count(*)
                                      from tof_shopping_baskets
                                          where sba_art_id = art_id and
                                                sba_uss_id = 1), 0) basket,
                     COALESCE( (select count(*)
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 0) list,
                     NVL( (select 1
                                  from dual
                               where exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                              aun_uss_id = 1 and
                                                          aun_kind = 'G')), 0) notice,
                acs_kv_status art_kv_status,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
               from tof_generic_articles
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     16 nljoin ga_des.des_lng_id 
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
              join tof_link_art_ga
                 on ga_id nljoin lag_ga_id 
               join tof_articles
                 on lag_art_id nljoin art_id and
                     art_ctm subrange(225 cast integer) = 1 and
                     art_sup_id in ({$suppID})
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id and
                     16 nljoin art_des.des_lng_id
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     255 nljoin art_des_uni.des_lng_id
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id
left outer join tof_suppliers sup_cou
                 on art_sup_id nljoin sup_cou.sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on art_sup_id nljoin sup_null.sup_id and
                     sup_null.sup_cou_id is null
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where ga_id in ({$gaID}) and
                     (1 = 3)
UNION
              select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text||' ('||ga_assembly_tex.tex_text||')' ga_assembly,
                     la_id,
                     laa_sort sort,
                     laa_sup_id bra_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) bra_brand,
                     COALESCE( (select sba_quantity
                                     from tof_shopping_baskets
                                         where sba_art_id = art_id and
                                               sba_uss_id = 1),
                                     0) basket,
                     COALESCE( (select sli_art_id
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 
                                     0) list,
                     NVL( (select 1
                                  from dual
                               where exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                              aun_uss_id = 1 and
                                                          aun_kind = 'G') or
                                      exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                          aun_uss_id = 1 and
                                                          aun_kind = 'S' and
                                                          aun_axl_id = {$carID})), 0) notice,
                acs_kv_status art_kv_status,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
               from tof_link_la_axl
              join tof_generic_articles
             on laa_ga_id nljoin ga_id
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     16 nljoin ga_des.des_lng_id
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id 
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
                join tof_link_art
                 on laa_la_id nljoin la_id
               join tof_articles
                 on la_art_id nljoin art_id and
                     art_ctm subrange(225 cast integer) = 1
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id  and
                     16 nljoin art_des.des_lng_id
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     255 nljoin art_des_uni.des_lng_id
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id
left outer join tof_suppliers sup_cou
                 on laa_sup_id nljoin sup_cou.sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on laa_sup_id nljoin sup_null.sup_id and
                     sup_null.sup_cou_id is null
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where laa_ga_id in ({$gaID}) and
                     laa_ctm subrange(225 cast integer) = 1 and
                     laa_axl_id = {$carID} and
                     laa_sup_id in ({$suppID}) and
                     (1 = 5)

UNION
              select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text||' ('||ga_assembly_tex.tex_text||')' ga_assembly,
                     la_id,
                     lam_sort sort,
                     lam_sup_id bra_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) bra_brand,
                     COALESCE( (select sba_quantity
                                     from tof_shopping_baskets
                                         where sba_art_id = art_id and
                                               sba_uss_id = 1),
                                     0) basket,
                     COALESCE( (select sli_art_id
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 
                                     0) list,
                     NVL( (select 1
                                  from dual
                               where exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                              aun_uss_id = 1 and
                                                          aun_kind = 'G') or
                                      exists (select 1
                                                    from tof_article_user_notes
                                                  where aun_art_id = art_id and
                                                          aun_uss_id = 1 and
                                                          aun_kind = 'S' and
                                                          aun_mrk_id = {$carID})), 0) notice,
                acs_kv_status art_kv_status,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
               from tof_link_la_mrk
              join tof_generic_articles
             on lam_ga_id nljoin ga_id
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     16 nljoin ga_des.des_lng_id
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id 
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
                join tof_link_art
                 on lam_la_id nljoin la_id
               join tof_articles
                 on la_art_id nljoin art_id and
                     art_ctm subrange(225 cast integer) = 1
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id  and
                     16 nljoin art_des.des_lng_id
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     255 nljoin art_des_uni.des_lng_id
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id
left outer join tof_suppliers sup_cou
                 on lam_sup_id nljoin sup_cou.sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on lam_sup_id nljoin sup_null.sup_id and
                     sup_null.sup_cou_id is null
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where lam_ga_id in ({$gaID}) and
                     lam_ctm subrange(225 cast integer) = 1 and
                     lam_mrk_id = {$carID} and
                     lam_sup_id in ({$suppID}) and
                     (1 = 6)


	
EOT;
	return $val;
}
function GETITEMINFO1($artID){
	$val = <<<KKK
	SELECT arl_display_nr ean_ean,   
	                  acs_pack_unit art_pack_unit,
	                  art_replacement,
	                  art_pack_selfservice,            
	                     art_accessory,
	                     art_material_mark,
	                  tex_text,
	                   acs_kv_status art_kv_status,
	                ACS_QUANTITY_PER_UNIT
	              FROM tof_articles
	left outer join tof_art_lookup
	                 on arl_art_id = art_id and
	                     arl_ctm subrange(225 cast integer) = 1 and
	                arl_kind = '5'
	left outer join tof_art_country_specifics
	             on art_id = acs_art_id
	            and 1 = acs_ctm subrange(225 cast integer)
	left outer join tof_designations
	                on des_id = acs_kv_status_des_id 
	                and des_lng_id = 16 
	left outer join tof_des_texts
	                on tex_id = des_tex_id
	             where art_id = {$artID}
	               and art_ctm subrange(225 CAST INTEGER) = 1

	
KKK;
	               return $val;
}
function GETIMAGEINFO($graID,$laID){
	$val  = <<<EOT
 SELECT * FROM( SELECT gra_sup_id,
         gra_id,   
         gra_doc_type,   
         gra_lng_id,   
         gra_type,   
         gra_norm,   
            gra_supplier_nr,
         gra_tab_nr,
         gra_grd_id,
            nvl(gra_tex.tex_text, gra_tex_uni.tex_text) gra_designation,
            lga_sort,
            2 sort
    FROM tof_graphics g
    JOIN tof_link_gra_art  
      ON gra_id = lga_gra_id 
     and lga_art_id = {$graID}
     and lga_ctm subrange (225 CAST INTEGER) =1
left outer join tof_designations gra_des
                 on gra_des_id nljoin gra_des.des_id and
                     gra_des.des_lng_id = 16
left outer join tof_des_texts gra_tex
                 on gra_des.des_tex_id nljoin gra_tex.tex_id
left outer join tof_designations gra_des_uni
                 on gra_des_id nljoin gra_des_uni.des_id and
                     gra_des_uni.des_lng_id = 255
left outer join tof_des_texts gra_tex_uni
                 on gra_des_uni.des_tex_id nljoin gra_tex_uni.tex_id
   where (   gra_lng_id = 16 or 
             ( gra_lng_id = 255 and not exists ( select gra_id
                                                   from tof_graphics g1
                                                  where g.gra_id = g1.gra_id
                                                    and  g1.gra_lng_id = 16) ) ) and
            not exists (select 1
                              from tof_ali_coordinates ali
                             where lga_gra_id nljoin ali.aco_gra_id and
                                     lga_art_id nljoin ali.aco_ali_art_id and
                                     (16 nljoin ali.aco_gra_lng_id or 
                                   (ali.aco_gra_lng_id = 255 and not exists (select 1
                                                                                  from tof_ali_coordinates ali_co
                                                                                  where ali_co.aco_gra_id = ali.aco_gra_id and
                                                                                                        ali_co.aco_ali_art_id = ali.aco_ali_art_id and
                                                                                       ali_co.aco_gra_lng_id = 16))))
     

UNION
  SELECT gra_sup_id,   
         gra_id,   
         gra_doc_type,   
         gra_lng_id,  
         gra_type,   
         gra_norm,   
         gra_supplier_nr,
            gra_tab_nr, 
         gra_grd_id,
            nvl(gra_tex.tex_text, gra_tex_uni.tex_text) gra_designation,
            lgl_sort lga_sort,
            1 sort
    FROM tof_graphics g
     JOIN tof_link_gra_la  
      ON gra_id = lgl_gra_id 
     and lgl_la_id = {$laID}
     and lgl_ctm subrange(225 CAST INTEGER) =1
left outer join tof_designations gra_des
                 on gra_des_id nljoin gra_des.des_id and
                     gra_des.des_lng_id = 16
left outer join tof_des_texts gra_tex
                 on gra_des.des_tex_id nljoin gra_tex.tex_id
left outer join tof_designations gra_des_uni
                 on gra_des_id nljoin gra_des_uni.des_id and
                     gra_des_uni.des_lng_id = 255
left outer join tof_des_texts gra_tex_uni
                 on gra_des_uni.des_tex_id nljoin gra_tex_uni.tex_id
   where (   gra_lng_id = 16 or 
             ( gra_lng_id = 255 and not exists ( select gra_id
                                                   from tof_graphics g1
                                                  where g.gra_id = g1.gra_id
                                                    and  g1.gra_lng_id = 16) ) ) and
            not exists (select 1
                              from tof_ali_coordinates ali
                             where lgl_gra_id nljoin ali.aco_gra_id and
                                     2814629 nljoin ali.aco_ali_art_id and
                                     (16 nljoin ali.aco_gra_lng_id or 
                                   (ali.aco_gra_lng_id = 255 and not exists (select 1
                                                                                  from tof_ali_coordinates ali_co
                                                                                  where ali_co.aco_gra_id = ali.aco_gra_id and
                                                                                                        ali_co.aco_ali_art_id = ali.aco_ali_art_id and
                                                                                        ali_co.aco_gra_lng_id = 16))))
     
order by sort, lga_sort )
LEFT JOIN  TOF_DOC_TYPES on TOF_DOC_TYPES.DOC_TYPE = gra_doc_type
	
EOT;
	return $val;
}
function GETIMAGE($imgID,$db_num){
	
	//echo  "params = ".$imgID." ".$db_num;
	
	$val = "select grd_graphic  from tof_gra_data_{$db_num} where grd_id ={$imgID}";

//echo "<br>".$val;
return $val;
}

function GETARTICLESEARCHBYID($art_name)
{
	$art_name = trim($art_name," ");
	$art_name = mb_convert_case($art_name, MB_CASE_UPPER);
	$ret = <<<EOT
	
  select distinct art_id,

                     ga_id,

                     ga_tex.tex_text gades,

                     ga_assembly_tex.tex_text ga_assembly,

                     art_sup_id sup_id,

                     arl_kind,

                     arl_display_nr,

                     art_replacement

              from tof_art_lookup

              join tof_articles

                 on arl_art_id nljoin art_id and

                     1 nljoin art_ctm subrange(225 cast integer)

              join tof_link_art_ga

                 on lag_art_id = art_id

               join tof_generic_articles

                 on ga_id = lag_ga_id and

                     ((ga_universal = 0 and

                      ga_id = ga_nr) or

                       ga_universal = 1)

              join tof_designations ga_des

                 on ga_des_id nljoin ga_des.des_id and

                     ga_des.des_lng_id = 16

              join tof_des_texts ga_tex

                 on ga_des.des_tex_id nljoin ga_tex.tex_id

left outer join tof_designations ga_assembly_des

                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and

                     16 nljoin ga_assembly_des.des_lng_id

left outer join tof_des_texts ga_assembly_tex

                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 

             where arl_ctm subrange(225 cast integer) = 1 and

                     arl_kind in ('1','2','3','4','5') and

( (0 = 1 and arl_search_number like '{$art_name}' ) or ( 0 = 0 and arl_search_number = '{$art_name}' ) ) and

                     (-1 = -1  or  ga_id = -1 )

UNION ALL

              select distinct art_id,

                     ga_id,

                     ga_tex.tex_text gades,

                     ga_assembly_tex.tex_text ga_assembly,

                     art_sup_id sup_id,

                     '0' arl_kind,

                     tsp_dealer_article_nr arl_display_nr,

                     art_replacement

              from tof_tecsel_dealers

              join tof_tecsel_prices

                 on tsd_id nljoin tsp_tsd_id and

                     tsp_ctm subrange(225 cast integer) = 1 and

( (0 = 1 and tsp_search_number like '{$art_name}' ) or ( 0 = 0 and  tsp_search_number = '{$art_name}' ) )

              join tof_articles

                 on tsp_art_id nljoin art_id and

                     1 nljoin art_ctm subrange(225 cast integer)

              join tof_link_art_ga

                 on lag_art_id = art_id

               join tof_generic_articles

                 on ga_id = lag_ga_id and

                     ((ga_universal = 0 and

                      ga_id = ga_nr) or

                       ga_universal = 1)

              join tof_designations ga_des

                 on ga_des_id nljoin ga_des.des_id and

                     ga_des.des_lng_id = 16

              join tof_des_texts ga_tex

                 on ga_des.des_tex_id nljoin ga_tex.tex_id

left outer join tof_designations ga_assembly_des

                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and

                     16 nljoin ga_assembly_des.des_lng_id

left outer join tof_des_texts ga_assembly_tex

                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 

             where tsd_id = -1 and

                     (-1 = -1  or  ga_id = -1 )

order by ga_id
EOT;
//echo $ret;	
	return $ret;
}
function GETMANUFACTORIESSEARCHBYID($art_name,$id_str){
		$art_name = trim($art_name," ");
		$art_name = mb_convert_case($art_name, MB_CASE_UPPER);
		$ret = <<<EOT
select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text|| nvl(' ('||ga_assembly_tex.tex_text||')', ' ') ga_assembly,
                     art_sup_id sup_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) sup_brand,
                     arl_kind,
                     arl_display_nr,
                     NVL( (select sba_quantity
                                from tof_shopping_baskets
                                  where sba_art_id = art_id and
                                      sba_uss_id = 1),
                             0) basket,
                     COALESCE( (select sli_art_id
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 
                                  (select 1
                                      from dual
                                     where acs_kv_status = '7'),
                                     0) list,
                     NVL( (select aun_art_id
                                        from tof_article_user_notes
                                     where aun_art_id = art_id and
                                                aun_uss_id = 1 ), 0) notice,
                acs_kv_status art_kv_status,
                     '' tsd_matchcode,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
              from tof_art_lookup
              join tof_articles
                 on arl_art_id nljoin art_id and
                     1 nljoin art_ctm subrange(225 cast integer)and
                arl_art_id in ({$id_str})
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id and
                     art_des.des_lng_id = 16
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     art_des_uni.des_lng_id = 255
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id
left outer join tof_suppliers sup_cou
                 on sup_cou.sup_id = art_sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on sup_null.sup_id = art_sup_id and
                     sup_null.sup_cou_id is null
              join tof_link_art_ga
                 on lag_art_id = art_id
                and (-1 = -1  or  lag_ga_id = -1 )
               join tof_generic_articles
                 on ga_id = lag_ga_id and
                     ((ga_universal = 0 and
                      ga_id = ga_nr) or
                       ga_universal = 1)
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     ga_des.des_lng_id = 16
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where arl_ctm subrange(225 cast integer) = 1 and
                     arl_kind in ('1','2','3','4','5') and
                     arl_search_number = '{$art_name}'
UNION ALL
              select distinct art_id,
                     art_article_nr,
                     nvl(art_tex.tex_text, art_tex_uni.tex_text) articledes,
                     ga_id,
                     ga_tex.tex_text gades,
                     ga_tex.tex_text|| nvl(' ('||ga_assembly_tex.tex_text||')', ' ') ga_assembly,
                     art_sup_id sup_id,
                     nvl(sup_cou.sup_brand, sup_null.sup_brand) sup_brand,
                     '0' arl_kind,
                     tsp_dealer_article_nr arl_display_nr,
                     NVL( (select sba_quantity
                                from tof_shopping_baskets
                                  where sba_art_id = art_id and
                                      sba_uss_id = 1),
                             0) basket,
                     COALESCE( (select sli_art_id
                                        from tof_shopping_lists
                                     where sli_art_id = art_id and
                                                sli_uss_id = 1), 
                                  (select 1
                                      from dual
                                     where acs_kv_status = '7'),
                                     0) list,
                     NVL( (select aun_art_id
                                        from tof_article_user_notes
                                     where aun_art_id = art_id and
                                                aun_uss_id = 1), 0) notice,
                acs_kv_status art_kv_status,
                     tsd_matchcode,
                     art_replacement,
                     nvl(sup_cou.sup_is_hess, sup_null.sup_is_hess) is_hess
              from tof_tecsel_dealers
              join tof_tecsel_prices
                 on tsd_id nljoin tsp_tsd_id and
                     tsp_ctm subrange(225 cast integer) = 1 and
                     tsp_search_number = '{$art_name}'
              join tof_articles
                 on tsp_art_id nljoin art_id and
                     1 nljoin art_ctm subrange(225 cast integer)
left outer join tof_designations art_des
                 on art_des_id nljoin art_des.des_id and
                     art_des.des_lng_id = 16
left outer join tof_des_texts art_tex
                 on art_des.des_tex_id nljoin art_tex.tex_id
left outer join tof_designations art_des_uni
                 on art_des_id nljoin art_des_uni.des_id and
                     art_des_uni.des_lng_id = 255
left outer join tof_des_texts art_tex_uni
                 on art_des_uni.des_tex_id nljoin art_tex_uni.tex_id
left outer join tof_suppliers sup_cou
                 on sup_cou.sup_id = art_sup_id and
                     sup_cou.sup_cou_id = 225
left outer join tof_suppliers sup_null
                 on sup_null.sup_id = art_sup_id and
                     sup_null.sup_cou_id is null
              join tof_link_art_ga
                 on lag_art_id = art_id
                and (-1 = -1  or  lag_ga_id = -1 )
               join tof_generic_articles
                 on ga_id = lag_ga_id and
                     ((ga_universal = 0 and
                      ga_id = ga_nr) or
                       ga_universal = 1)
              join tof_designations ga_des
                 on ga_des_id nljoin ga_des.des_id and
                     ga_des.des_lng_id = 16
              join tof_des_texts ga_tex
                 on ga_des.des_tex_id nljoin ga_tex.tex_id
left outer join tof_designations ga_assembly_des
                 on ga_des_id_assembly nljoin ga_assembly_des.des_id and
                     16 nljoin ga_assembly_des.des_lng_id
left outer join tof_des_texts ga_assembly_tex
                 on ga_assembly_des.des_tex_id nljoin ga_assembly_tex.tex_id 
left outer join tof_art_country_specifics
             on art_id = acs_art_id
            and 1 = acs_ctm subrange(225 cast integer)
             where tsd_id = -1
                     		order by SUP_BRAND
		
EOT;
 return $ret;
}


// Константы
define('COU_ID',187);//������ �������
//тип поиска при обращении к JSON данным 
define('SEARCHTYPE_SEARCH_BY_INDEX',"index");


