--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

-- Started on 2022-03-07 07:30:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA IF NOT EXISTS public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 3333 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 16403)
-- Name: bank_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bank_account (
    account_id text NOT NULL,
    customer_id integer NOT NULL,
    status numeric
);


ALTER TABLE public.bank_account OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16408)
-- Name: cash_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cash_account (
    cash_account_id bigint NOT NULL,
    account_id text NOT NULL,
    balance numeric NOT NULL,
    currency_code text,
    available_balance numeric
);


ALTER TABLE public.cash_account OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16441)
-- Name: all_cash_accounts; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.all_cash_accounts AS
 SELECT bank_account.account_id,
    bank_account.customer_id,
    cash_account.cash_account_id,
    cash_account.balance,
    cash_account.currency_code,
    cash_account.available_balance
   FROM (public.bank_account
     JOIN public.cash_account ON ((bank_account.account_id = cash_account.account_id)));


ALTER TABLE public.all_cash_accounts OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16417)
-- Name: cash_account_cash_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.cash_account ALTER COLUMN cash_account_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.cash_account_cash_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 214 (class 1259 OID 24623)
-- Name: transaction_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction_logs (
    transaction_id bigint NOT NULL,
    cash_account_id bigint NOT NULL,
    direction smallint NOT NULL,
    amount bigint NOT NULL,
    status smallint NOT NULL,
    transaction_time timestamp without time zone NOT NULL,
    description text NOT NULL
);


ALTER TABLE public.transaction_logs OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 24622)
-- Name: transaction_logs_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.transaction_logs ALTER COLUMN transaction_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.transaction_logs_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 215 (class 1259 OID 24628)
-- Name: vw_transaction_logs; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vw_transaction_logs AS
 SELECT ca.account_id,
    ca.currency_code,
    tl.transaction_id,
    tl.cash_account_id,
    tl.direction,
    tl.amount,
    tl.status,
    tl.transaction_time,
    tl.description
   FROM (public.transaction_logs tl
     JOIN public.cash_account ca ON ((tl.cash_account_id = ca.cash_account_id)));


ALTER TABLE public.vw_transaction_logs OWNER TO postgres;

--
-- TOC entry 3182 (class 2606 OID 16428)
-- Name: bank_account bank_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_pkey PRIMARY KEY (account_id);


--
-- TOC entry 3186 (class 2606 OID 16414)
-- Name: cash_account cash_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cash_account
    ADD CONSTRAINT cash_account_pkey PRIMARY KEY (cash_account_id);


--
-- TOC entry 3184 (class 2606 OID 16432)
-- Name: bank_account constraint_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT constraint_name UNIQUE (account_id);


-- Completed on 2022-03-07 07:30:47

--
-- PostgreSQL database dump complete
--

